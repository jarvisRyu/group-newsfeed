package com.cordingrecipe.groupnewsfeed.friend.service;


import com.cordingrecipe.groupnewsfeed.common.advice.CustomException;
import com.cordingrecipe.groupnewsfeed.common.advice.ErrorCode;
import com.cordingrecipe.groupnewsfeed.friend.dto.CreateFriendRequestDto;
import com.cordingrecipe.groupnewsfeed.friend.dto.CreateFriendResponseDto;
import com.cordingrecipe.groupnewsfeed.friend.dto.FriendResponseDto;
import com.cordingrecipe.groupnewsfeed.friend.dto.FriendViewDto;
import com.cordingrecipe.groupnewsfeed.friend.entity.Friend;
import com.cordingrecipe.groupnewsfeed.friend.repository.FriendRepository;
import com.cordingrecipe.groupnewsfeed.user.entity.User;
import com.cordingrecipe.groupnewsfeed.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.cordingrecipe.groupnewsfeed.friend.entity.Friend.FriendRequestStatus.ACCEPTED;

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
        Long toUserId = createFriendRequestDto.getToUserId();
        User toUser = userRepository.findByIdOrElseThrow(toUserId);

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
    public FriendResponseDto acceptFriendRequest(Long toUserId,Long fromUserId) {

        Friend friendRequest = friendRepository.findByFromUserIdAndToUserIdAndStatus(
                fromUserId, toUserId, Friend.FriendRequestStatus.PENDING).orElseThrow(() ->
                new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));


        //요청 수락 권한 없음
        friendRequest.ensureReceiverIs(toUserId);

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

        User fromUser = userRepository.findByIdOrElseThrow(toUserId);
        User toUser = userRepository.findByIdOrElseThrow(fromUserId);

        Friend friend = new Friend(fromUser, toUser);
        friend.accepted();
        friendRepository.save(friend);

        return new FriendResponseDto(friendRequest);
    }

    //친구 요청 거절
    @Transactional
    public FriendResponseDto rejectFriendRequest(Long toUserId, Long fromUserId) {

        // 친구 요청 찾기 (from → to 방향)
        Friend friendRequest = friendRepository.findByFromUserIdAndToUserIdAndStatus(
                fromUserId, toUserId, Friend.FriendRequestStatus.PENDING
        ).orElseThrow(() ->
                new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND)
        );

        // 권한 확인
        friendRequest.ensureReceiverIs(toUserId);

        // 이미 친구 상태면 거절 불가
        if (friendRepository.isFriendInStatus(fromUserId, toUserId, ACCEPTED)) {
            throw new CustomException(ErrorCode.FRIEND_ALREADY_ACCEPTED);
        }

        // 상태만 REJECTED로 변경
        friendRequest.rejectRequest(); // 내부에서 this.status = REJECTED
        friendRepository.save(friendRequest);

        return new FriendResponseDto(friendRequest);
    }

    @Transactional
    public List<FriendViewDto> getReceivedRequests(Long userId) {
        //양방향으로 저장된 친구 목록 전부 가져옴
        List<Friend> allFriends = new ArrayList<>();
        allFriends.addAll(friendRepository.findByFromUserIdAndStatus(userId, ACCEPTED));
        allFriends.addAll(friendRepository.findByToUserIdAndStatus(userId, ACCEPTED));

        //중복 제거 항상 fromId < toId 인 경우
        return allFriends.stream()
                .filter(friend -> {
                    Long fromId = friend.getFromUser().getId();
                    Long toId = friend.getToUser().getId();

                    return fromId < toId;
                })
                .map(friend -> {
                    // 나와 연결된 상대방 친구만 추출
                    Long fromId = friend.getFromUser().getId();

                    // 내가 from이면 → to가 친구 / 내가 to면 → from이 친구
                    User other = fromId.equals(userId)
                            ? friend.getToUser()
                            : friend.getFromUser();

                    return new FriendViewDto(other.getId(), other.getUsername());
                })
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
    public FriendViewDto findFriend(Long myId, Long targetUserId) {
        Optional<Friend> optionalFriend = friendRepository
                .findByFromUserIdAndToUserIdAndStatus(myId, targetUserId, ACCEPTED);

        if (optionalFriend.isEmpty()) {
            friendRepository.findByFromUserIdAndToUserIdAndStatus(targetUserId, myId, ACCEPTED);
        }

        Friend friend = optionalFriend.orElseThrow(() ->
                new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND)
        );

        return new FriendViewDto(friend.getToUser().getId(), friend.getToUser().getUsername());
    }


    //친구 삭제
    @Transactional
    public void deleteFriend(Long myId, Long targetUserId) {

        Friend friend = friendRepository
                .findByFromUserIdAndToUserId(myId,targetUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        if(!targetUserId.equals(friend.getToUser().getId())&& !targetUserId.equals(friend.getFromUser().getId())) {
            throw new CustomException(ErrorCode.FRIEND_DELETE_FORBIDDEN);
        }

        friendRepository.delete(friend);

        friendRepository.findByFromUserIdAndToUserId(
                friend.getToUser().getId(),
                friend.getFromUser().getId()
        ).ifPresent(friendRepository::delete);
    }

}


