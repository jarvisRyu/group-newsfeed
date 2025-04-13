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

    @Transactional
    public CreateFriendResponseDto createFriend(Long userId, CreateFriendRequestDto createFriendRequestDto) {

        User loginUser = userRepository.findByIdOrElseThrow(userId);

        Long toUserId = createFriendRequestDto.getToUserId();
        User toUser = userRepository.findByIdOrElseThrow(toUserId);

        if (loginUser.getId().equals(toUser.getId())) {
            throw new CustomException(ErrorCode.FRIEND_SELF_REQUEST);
        }

        boolean alreadyRequested = friendRepository.existsByFromUserAndToUser(loginUser, toUser);
        if (alreadyRequested) {
            throw new CustomException(ErrorCode.FRIEND_ALREADY_REQUESTED);
        }

        Friend friend = new Friend(loginUser, toUser);
        friend.pending();
        friendRepository.save(friend);

        return new CreateFriendResponseDto(friend);

    }

    @Transactional
    public FriendResponseDto acceptFriendRequest(Long toUserId,Long fromUserId) {

        Friend friendRequest = friendRepository.findByFromUserIdAndToUserIdAndStatus(
                fromUserId, toUserId, Friend.FriendRequestStatus.PENDING).orElseThrow(() ->
                new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        friendRequest.ensureReceiverIs(toUserId);

        if (friendRequest.getStatus() == ACCEPTED) {
            throw new CustomException(ErrorCode.FRIEND_ALREADY_ACCEPTED);
        }

        friendRequest.acceptIfPending();
        friendRepository.save(friendRequest);

        friendRepository.findByFromUserIdAndToUserId(friendRequest.getToUser().getId(), friendRequest.getFromUser().getId())
                .ifPresent(Friend::acceptIfPending);

        User fromUser = userRepository.findByIdOrElseThrow(toUserId);
        User toUser = userRepository.findByIdOrElseThrow(fromUserId);

        Friend friend = new Friend(fromUser, toUser);
        friend.accepted();
        friendRepository.save(friend);

        return new FriendResponseDto(friendRequest);
    }

    @Transactional
    public FriendResponseDto rejectFriendRequest(Long toUserId, Long fromUserId) {

        Friend friendRequest = friendRepository.findByFromUserIdAndToUserIdAndStatus(
                fromUserId, toUserId, Friend.FriendRequestStatus.PENDING
        ).orElseThrow(() ->
                new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND)
        );

        friendRequest.ensureReceiverIs(toUserId);

        friendRequest.rejectRequest();
        friendRepository.save(friendRequest);

        return new FriendResponseDto(friendRequest);
    }

    @Transactional
    public List<FriendViewDto> getReceivedRequests(Long userId) {

        List<Friend> allFriends = new ArrayList<>();
        allFriends.addAll(friendRepository.findByFromUserIdAndStatus(userId, ACCEPTED));
        allFriends.addAll(friendRepository.findByToUserIdAndStatus(userId, ACCEPTED));

        return allFriends.stream()
                .filter(friend -> {
                    Long fromId = friend.getFromUser().getId();
                    Long toId = friend.getToUser().getId();

                    return fromId < toId;
                })
                .map(friend -> {
                    Long fromId = friend.getFromUser().getId();

                    User other = fromId.equals(userId)
                            ? friend.getToUser()
                            : friend.getFromUser();

                    return new FriendViewDto(other.getId(), other.getUsername());
                })
                .toList();
    }

    public List<FriendResponseDto> getPendingFriendRequests(Long userId) {

        List<Friend> toAccepted = friendRepository
                .findByToUserIdAndStatus(userId, Friend.FriendRequestStatus.PENDING);

        return toAccepted.stream()
                .map(FriendResponseDto::new)
                .toList();
    }

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

    @Transactional
    public void deleteFriend(Long myId, Long targetUserId) {

        Friend friend = friendRepository
                .findByFromUserIdAndToUserId(myId,targetUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

        if(!targetUserId.equals(friend.getToUser().getId()) && !targetUserId.equals(friend.getFromUser().getId())) {
            throw new CustomException(ErrorCode.FRIEND_DELETE_FORBIDDEN);
        }

        friendRepository.delete(friend);

        friendRepository.findByFromUserIdAndToUserId(
                friend.getToUser().getId(),
                friend.getFromUser().getId()
        ).ifPresent(friendRepository::delete);
    }

}


