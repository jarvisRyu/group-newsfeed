package com.cordingrecipe.groupnewsfeed.friend.service;


import com.cordingrecipe.groupnewsfeed.common.advice.CustomException;
import com.cordingrecipe.groupnewsfeed.common.advice.ErrorCode;
import com.cordingrecipe.groupnewsfeed.friend.dto.CreateFriendRequestDto;
import com.cordingrecipe.groupnewsfeed.friend.dto.CreateFriendResponseDto;
import com.cordingrecipe.groupnewsfeed.friend.dto.FriendResponseDto;
import com.cordingrecipe.groupnewsfeed.friend.entity.Friends;
import com.cordingrecipe.groupnewsfeed.friend.repository.FriendRepository;
import com.cordingrecipe.groupnewsfeed.friend.validator.FriendValidator;
import com.cordingrecipe.groupnewsfeed.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.cordingrecipe.groupnewsfeed.friend.entity.Friends.FriendRequestStatus.ACCEPTED;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final FriendValidator friendValidator;

    //친구 요청
    //ManyToOne관계는 객체 User를 직접 넣어줘야함. 객체 간의 연관관계를 이용해서 자동으로 관리해주기 때문에
    public CreateFriendResponseDto createFriend(Long userId, CreateFriendRequestDto createFriendRequestDto) {

        //존재하지 않는 사용자
        User loginUser = friendValidator.validateUserExist(userId);

        //수신유저 존재하지 않음
        Long receiverId = createFriendRequestDto.getReceiverId();
        User toUser = friendValidator.validateUserExist(receiverId);

        //자기 자신에게 친구요청
        if (loginUser.getId().equals(toUser.getId())) {
            throw new CustomException(ErrorCode.FRIEND_SELF_REQUEST);
        }

        //친구 관계, 요청한 이력있는지 중복검사
        boolean alredyRequested = friendRepository.existsByFromUserAndToUser(loginUser, toUser); //id값을 받아온거 , 보내는 사람
        if (alredyRequested) {
            throw new CustomException(ErrorCode.FRIEND_ALREADY_REQUESTED);
        }

        Friends friend = Friends.pending(loginUser, toUser);

        friendRepository.save(friend);

        return new CreateFriendResponseDto(friend);

    }

    //친구 요청 수락
    public FriendResponseDto acceptFriendRequest(Long userId, Long requesterId) {

        Friends friendRequest = friendRepository.findByFromUserIdAndToUserIdAndStatus(
                requesterId, userId, Friends.FriendRequestStatus.PENDING).orElseThrow(() ->
                new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        //요청 수락 권한 없음
        friendValidator.validateReceiver(userId, friendRequest);

        //이미 친구 상태의 경우
        if (friendRequest.getStatus() == ACCEPTED) {
            throw new CustomException(ErrorCode.FRIEND_ALREADY_ACCEPTED);
        }

        // 친구 요청 수락처리
        friendRequest.acceptIfNotAccepted(); //set대신 status == accepted처리
        friendRepository.save(friendRequest);

        //반대 방향 존재 여부 확인
        //옵셔널 간결하게 처리
        friendRepository.findByFromUserIdAndToUserId(friendRequest.getToUser().getId(), friendRequest.getFromUser().getId())
                .ifPresent(Friends::acceptIfNotAccepted);

        User fromUser = friendValidator.validateUserExist(userId);
        User toUser = friendValidator.validateUserExist(requesterId);

        //반대 방향에서 친구 객체가 생성되어야 하니까 FRIENDS를 하나 더 만듦.
        Friends friend = Friends.accepted(fromUser, toUser);
        friendRepository.save(friend);

        return new FriendResponseDto(friendRequest);
    }

    //친구 요청 거절
    public FriendResponseDto rejectFriendRequest(Long userId, Long requesterId) {

        //친구 요청이 존재하지 않습니다.
        Friends friendRequest = friendRepository.findByFromUserIdAndToUserIdAndStatus(
                requesterId, userId, Friends.FriendRequestStatus.PENDING).orElseThrow(() ->
                new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        //요청 수락 권한 없음
        friendValidator.validateReceiver(userId, friendRequest);

        //이미 친구상태면 요청 또 보낼 수 없음 (양방향)
        //디폴트메서드로 변경
        boolean isAlreadyFriend =
                friendRepository.existsByFromUserIdAndToUserIdAndStatus(userId, requesterId, Friends.FriendRequestStatus.ACCEPTED) ||
                        friendRepository.existsByFromUserIdAndToUserIdAndStatus(requesterId, userId, Friends.FriendRequestStatus.ACCEPTED);

        if (isAlreadyFriend) {
            throw new CustomException(ErrorCode.FRIEND_ALREADY_ACCEPTED);
        }

        friendRequest.rejectRequest(); //set대신 status == 거절처리

        User fromUser = friendValidator.validateUserExist(userId);
        User toUser = friendValidator.validateUserExist(requesterId);

        Friends friend = Friends.rejected(fromUser, toUser);
        friendRepository.save(friend);
        friendRepository.save(friendRequest);

        return new FriendResponseDto(friendRequest);

    }

    //내 친구 목록 조회
    @Transactional
    public List<FriendResponseDto> getReceivedRequests(Long userId) {

        List<Friends> fromAccepted = friendRepository
                .findByFromUserIdAndStatus(userId, ACCEPTED);
        List<Friends> toAccepted = friendRepository
                .findByToUserIdAndStatus(userId, ACCEPTED);

        List<Friends> all = new ArrayList<>();
        all.addAll(fromAccepted);
        all.addAll(toAccepted);

        return all.stream()
                .map(FriendResponseDto::new)
                .toList();
    }

    //나한테 친구 신청한 목록 조회
    public List<FriendResponseDto> getPendingFriendRequests(Long userId) {

        List<Friends> toAccepted = friendRepository
                .findByToUserIdAndStatus(userId, Friends.FriendRequestStatus.PENDING);

        return toAccepted.stream()
                .map(FriendResponseDto::new)
                .toList();
    }

    //친구 단일 조회
    public FriendResponseDto findFriends(Long fromUserId, Long toUserId, Long userId) {

        if (!userId.equals(fromUserId) && !userId.equals(toUserId)) {
            throw new CustomException(ErrorCode.FRIEND_ACCESS_DENIED);
        }

        Friends friend = friendRepository
                .findByFromUserIdAndToUserIdAndStatus(fromUserId, toUserId, ACCEPTED)
                .orElseThrow(() -> new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        return new FriendResponseDto(friend);
    }


    //친구 삭제
    public void deleteFriend(Long myId, Long userId) {

        Friends friend = friendRepository
                .findByFromUserIdAndToUserId(myId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        if(!userId.equals(friend.getToUser().getId())&& !userId.equals(friend.getFromUser().getId())) {
            throw new CustomException(ErrorCode.FRIEND_DELETE_FORBIDDEN);
        }

        friendRepository.delete(friend);

        //반대방향 처리 값이 존재해야 delete
        friendRepository.findByFromUserIdAndToUserId(
                friend.getToUser().getId(),
                friend.getFromUser().getId()
        ).ifPresent(friendRepository::delete);
    }

}


//트랜젝션을 중간에 추가. 디폴트메서드 이용