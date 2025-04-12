package com.cordingrecipe.groupnewsfeed.friend.service;


import com.cordingrecipe.groupnewsfeed.common.advice.CustomException;
import com.cordingrecipe.groupnewsfeed.common.advice.ErrorCode;
import com.cordingrecipe.groupnewsfeed.friend.dto.CreateFriendRequestDto;
import com.cordingrecipe.groupnewsfeed.friend.dto.CreateFriendResponseDto;
import com.cordingrecipe.groupnewsfeed.friend.dto.FriendResponseDto;
import com.cordingrecipe.groupnewsfeed.friend.entity.Friend;
import com.cordingrecipe.groupnewsfeed.friend.repository.FriendRepository;
import com.cordingrecipe.groupnewsfeed.user.entity.User;
import com.cordingrecipe.groupnewsfeed.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.cordingrecipe.groupnewsfeed.friend.entity.Friend.FriendRequestStatus.*;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    //친구 요청
    //ManyToOne관계는 객체 User를 직접 넣어줘야함. 객체 간의 연관관계를 이용해서 자동으로 관리해주기 때문에
    @Transactional
    public CreateFriendResponseDto createFriend(Long userId, CreateFriendRequestDto createFriendRequestDto) {

        //존재하지 않는 사용자
        User loginUser = userRepository.findByIdOrElseThrow(userId);

        //수신유저 존재하지 않음
        Long receiverId = createFriendRequestDto.getReceiverId();
        User toUser = userRepository.findByIdOrElseThrow(receiverId);

        //자기 자신에게 친구요청
        if (loginUser.getId().equals(toUser.getId())) {
            throw new CustomException(ErrorCode.FRIEND_SELF_REQUEST);
        }

        //친구 관계, 요청한 이력있는지 중복검사
        boolean alredyRequested = friendRepository.existsByFromUserAndToUser(loginUser, toUser); //id값을 받아온거 , 보내는 사람
        if (alredyRequested) {
            throw new CustomException(ErrorCode.FRIEND_ALREADY_REQUESTED);
        }

        Friend friend = new Friend(loginUser, toUser);
        friend.pending();
        friendRepository.save(friend);

        return new CreateFriendResponseDto(friend);

    }

    //친구 요청 수락
    @Transactional
    public FriendResponseDto acceptFriendRequest(Long userId, Long requesterId) {

        Friend friendRequest = friendRepository.findByFromUserIdAndToUserIdAndStatus(
                requesterId, userId, Friend.FriendRequestStatus.PENDING).orElseThrow(() ->
                new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        //요청 수락 권한 없음
        friendRequest.ensureReceiverIs(userId);

        //이미 친구 상태의 경우
        if (friendRequest.getStatus() == ACCEPTED) {
            throw new CustomException(ErrorCode.FRIEND_ALREADY_ACCEPTED);
        }

        // 친구 요청 수락처리
        friendRequest.acceptIfPending(); //set대신 status == accepted처리
        friendRepository.save(friendRequest);

        //반대 방향 존재 여부 확인
        friendRepository.findByFromUserIdAndToUserId(friendRequest.getToUser().getId(), friendRequest.getFromUser().getId())
                .ifPresent(Friend::acceptIfPending);


        User fromUser = userRepository.findByIdOrElseThrow(userId);
        User toUser = userRepository.findByIdOrElseThrow(requesterId);

        Friend friend = new Friend(fromUser, toUser);
        friend.accepted();
        friendRepository.save(friend);

        return new FriendResponseDto(friendRequest);
    }

    //친구 요청 거절
    @Transactional
    public FriendResponseDto rejectFriendRequest(Long userId, Long requesterId) {

        //친구 요청이 존재하지 않습니다.
        Friend friendRequest = friendRepository.findByFromUserIdAndToUserIdAndStatus(
                requesterId, userId, Friend.FriendRequestStatus.PENDING).orElseThrow(() ->
                new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        //요청 수락 권한 없음

        friendRequest.ensureReceiverIs(userId);
        //이미 친구상태면 요청 또 보낼 수 없음 (양방향)
        if(friendRepository.isFriendInStatus(userId, requesterId,ACCEPTED)) {
            throw new CustomException(ErrorCode.FRIEND_ALREADY_ACCEPTED);
        }

        friendRequest.rejectRequest(); //set대신 status == 거절처리

        User fromUser = userRepository.findByIdOrElseThrow(userId);
        User toUser = userRepository.findByIdOrElseThrow(requesterId);


        Friend friend = new Friend(fromUser,toUser);
        friend.rejected();
        friendRepository.save(friend);


        friendRequest.rejected();
        friendRepository.save(friendRequest);

        return new FriendResponseDto(friendRequest);

    }

    //내 친구 목록 조회
    @Transactional
    public List<FriendResponseDto> getReceivedRequests(Long userId) {

        List<Friend> fromAccepted = friendRepository
                .findByFromUserIdAndStatus(userId, ACCEPTED);
        List<Friend> toAccepted = friendRepository
                .findByToUserIdAndStatus(userId, ACCEPTED);

        List<Friend> all = new ArrayList<>();
        all.addAll(fromAccepted);
        all.addAll(toAccepted);

        return all.stream()
                .map(FriendResponseDto::new)
                .toList();
    }

    //나한테 친구 신청한 목록 조회
    public List<FriendResponseDto> getPendingFriendRequests(Long userId) {

        List<Friend> toAccepted = friendRepository
                .findByToUserIdAndStatus(userId, Friend.FriendRequestStatus.PENDING);

        return toAccepted.stream()
                .map(FriendResponseDto::new)
                .toList();
    }

    //친구 단일 조회
    public FriendResponseDto findFriends(Long fromUserId, Long toUserId, Long userId) {

        if (!userId.equals(fromUserId) && !userId.equals(toUserId)) {
            throw new CustomException(ErrorCode.FRIEND_ACCESS_DENIED);
        }

        Friend friend = friendRepository
                .findByFromUserIdAndToUserIdAndStatus(fromUserId, toUserId, ACCEPTED)
                .orElseThrow(() -> new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        return new FriendResponseDto(friend);
    }


    //친구 삭제
    @Transactional
    public void deleteFriend(Long myId, Long userId) {

        Friend friend = friendRepository
                .findByFromUserIdAndToUserId(myId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        if(!userId.equals(friend.getToUser().getId())&& !userId.equals(friend.getFromUser().getId())) {
            throw new CustomException(ErrorCode.FRIEND_DELETE_FORBIDDEN);
        }

        friendRepository.delete(friend);

        //반대방향 처리 값이 존재해야 delete있으면(friend 객체가 존재하면) → delete()로 삭제해라"
        friendRepository.findByFromUserIdAndToUserId(
                friend.getToUser().getId(),
                friend.getFromUser().getId()
        ).ifPresent(friendRepository::delete);
    }

}


//friend클래스 이름 변경
//행위 메서드 생성
//acceptIfPending()으로 메서드 이름 변경.
//이미 친구상태면 요청 또 보낼 수 없음 (양방향) 메서드 repository책임지도록 리펙토링.
//validate클래스 삭제 후 검증 책임이 Friend 도메인 객체로. Repository 인터페이스에 boolean로직 정의
//friend테이블 외래키 이름 변경