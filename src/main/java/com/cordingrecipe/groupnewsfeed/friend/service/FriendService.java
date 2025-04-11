package com.cordingrecipe.groupnewsfeed.friend.service;


import com.cordingrecipe.groupnewsfeed.common.advice.CustomException;
import com.cordingrecipe.groupnewsfeed.common.advice.ErrorCode;
import com.cordingrecipe.groupnewsfeed.friend.dto.CreateFriendRequestDto;
import com.cordingrecipe.groupnewsfeed.friend.dto.CreateFriendResponseDto;
import com.cordingrecipe.groupnewsfeed.friend.entity.Friends;
import com.cordingrecipe.groupnewsfeed.friend.repository.FriendRepository;
import com.cordingrecipe.groupnewsfeed.friend.validator.FriendValidator;
import com.cordingrecipe.groupnewsfeed.user.entity.User;
import com.cordingrecipe.groupnewsfeed.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.cordingrecipe.groupnewsfeed.friend.entity.Friends.FriendRequestStatus.ACCEPTED;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final FriendValidator friendValidator;

    //친구 요청
    //ManyToOne관계는 객체 User를 직접 넣어줘야함. 객체 간의 연관관계를 이용해서 자동으로 관리해주기 때문에
    public CreateFriendResponseDto createFriend(Long userId, CreateFriendRequestDto createFriendRequestDto) {

        //존재하지 않는 사용자
        User loginUser = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.USER_NOT_FOUND)); //id값을 받아오는거


        Long receiverId = createFriendRequestDto.getReceiverId();
        //수신유저 존재하지 않음
        User toUser = userRepository.findById(receiverId).orElseThrow(() ->
                new CustomException(ErrorCode.USER_NOT_FOUND));

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
    public CreateFriendResponseDto acceptFriendRequest(Long userId, Long requesterId) {

        Friends friendRequest = friendRepository.findByFromUserIdAndToUserIdAndStatus(
                requesterId, userId, Friends.FriendRequestStatus.PENDING).orElseThrow(() ->
                new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        //요청 수락 권한 없음
        if (!friendRequest.getToUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FRIEND_ACCESS_DENIED);
        }

        //이미 친구 상태의 경우
        if (friendRequest.getStatus() == ACCEPTED) {
            throw new CustomException(ErrorCode.FRIEND_ALREADY_ACCEPTED);
        }

        // 친구 요청 수락처리
        friendRequest.setStatus(ACCEPTED);
        friendRepository.save(friendRequest);

        //반대 방향 존재 여부 확인
        Optional<Friends> reverseOpt = friendRepository
                .findByFromUserIdAndToUserId(userId, requesterId);

        //b -> a요청이 DB에 있으면 accepted로 바꿔야함.
        //set사용으로 인한 문제가 발생할 수 있기 때문에 수정고민중
        reverseOpt.ifPresent(Friends::acceptIfNotAccepted);

        User fromUser = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        User toUser = userRepository.findById(requesterId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        //반대 방향에서 친구 객체가 생성되어야 하니까 FRIENDS를 하나 더 만듦.
        Friends friend = Friends.accepted(fromUser, toUser);
        friendRepository.save(friend);

        return new CreateFriendResponseDto(friendRequest);
    }

    //친구 요청 거절
    public CreateFriendResponseDto rejectFriendRequest(Long userId, Long requesterId) {
        User loginUserEntity = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.USER_NOT_FOUND));

        //친구 요청이 존재하지 않습니다.
        Friends friendRequest = friendRepository.findByFromUserIdAndToUserIdAndStatus(
                requesterId, userId, Friends.FriendRequestStatus.PENDING).orElseThrow(() ->
                new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        //요청 수락 권한 없음
        if (!friendRequest.getToUser().equals(loginUserEntity)) {
            throw new CustomException(ErrorCode.FRIEND_ACCESS_DENIED);
        }

        //이미 친구상태면 요청 또 보낼 수 없음 (양방향)
        boolean isAlreadyFriend =
                friendRepository.existsByFromUserIdAndToUserIdAndStatus(userId, requesterId, Friends.FriendRequestStatus.ACCEPTED) ||
                        friendRepository.existsByFromUserIdAndToUserIdAndStatus(requesterId, userId, Friends.FriendRequestStatus.ACCEPTED);

        if (isAlreadyFriend) {
            throw new CustomException(ErrorCode.FRIEND_ALREADY_ACCEPTED);
        }

        friendRequest.setStatus(Friends.FriendRequestStatus.REJECTED);
        friendRepository.save(friendRequest);

        return new CreateFriendResponseDto(friendRequest);

    }

    //내 친구 목록 조회
    @Transactional
    public List<CreateFriendResponseDto> getReceivedRequests(Long userId) {

        List<Friends> fromAccepted = friendRepository
                .findByFromUserIdAndStatus(userId, ACCEPTED);
        List<Friends> toAccepted = friendRepository
                .findByToUserIdAndStatus(userId, ACCEPTED);

        List<Friends> all = new ArrayList<>();
        all.addAll(fromAccepted);
        all.addAll(toAccepted);

        return all.stream()
                .map(CreateFriendResponseDto::new)
                .toList();

    }

    //나한테 친구 신청한 목록 조회
    public List<CreateFriendResponseDto> getPendingFriendRequests(Long userId) {

        List<Friends> toAccepted = friendRepository
                .findByToUserIdAndStatus(userId, Friends.FriendRequestStatus.PENDING);

        return toAccepted.stream()
                .map(CreateFriendResponseDto::new)
                .toList();
    }
    //친구 단일 조회
    public CreateFriendResponseDto findFriends(Long fromUserId, Long toUserId, Long userId) {

        if (!userId.equals(fromUserId) && !userId.equals(toUserId)) {
            throw new CustomException(ErrorCode.FRIEND_ACCESS_DENIED);
        }

        Optional<Friends> friendOpt = friendRepository
                .findByFromUserIdAndToUserIdAndStatus(fromUserId, toUserId, ACCEPTED)
                .or(() -> friendRepository.findByFromUserIdAndToUserIdAndStatus(toUserId, fromUserId, ACCEPTED));

        Friends friend = friendOpt.orElseThrow(() ->
                new CustomException(ErrorCode.FRIEND_NOT_FOUND));

        return new CreateFriendResponseDto(friend);
    }



    //친구 삭제
    public void deleteFriend(Long myId, Long userId) {
        Optional<Friends> friendOpt = friendRepository
                .findByFromUserIdAndToUserId(myId, userId)
                .or(() -> friendRepository.findByFromUserIdAndToUserId(userId, myId));

        Friends friend = friendOpt.orElseThrow(() ->
                new CustomException(ErrorCode.FRIEND_NOT_FOUND));

        if(!userId.equals(friend.getToUser().getId())&& !userId.equals(friend.getFromUser().getId())) {
            throw new CustomException(ErrorCode.FRIEND_DELETE_FORBIDDEN);
        }
        friendRepository.delete(friend);

        friendRepository.findByFromUserIdAndToUserId(
                friend.getToUser().getId(), // 반대방향
                friend.getFromUser().getId()
        ).ifPresent(friendRepository::delete);
    }

}


