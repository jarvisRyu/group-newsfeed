package com.cordingrecipe.groupnewsfeed.friend.validator;

import com.cordingrecipe.groupnewsfeed.common.advice.CustomException;
import com.cordingrecipe.groupnewsfeed.common.advice.ErrorCode;
import com.cordingrecipe.groupnewsfeed.friend.entity.Friends;
import com.cordingrecipe.groupnewsfeed.friend.repository.FriendRepository;
import com.cordingrecipe.groupnewsfeed.user.entity.User;
import com.cordingrecipe.groupnewsfeed.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.cordingrecipe.groupnewsfeed.friend.entity.Friends.FriendRequestStatus.ACCEPTED;
import static com.cordingrecipe.groupnewsfeed.friend.entity.Friends.FriendRequestStatus.PENDING;


@Component
@RequiredArgsConstructor
public class FriendValidator {

    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    public User validateUserExist(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public Friends validateFriendRequestExist(Long fromId, Long toId) {
        return friendRepository.findByFromUserIdAndToUserIdAndStatus(fromId, toId, PENDING)
                .orElseThrow(() -> new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));
    }

    public void validateAccess(User loginUser, Friends friend) {
        if (!friend.getToUser().equals(loginUser)) {
            throw new CustomException(ErrorCode.FRIEND_ACCESS_DENIED);
        }
    }

    public void validateNotAlreadyFriend(Long a, Long b) {
        boolean alreadyFriend =
                friendRepository.existsByFromUserIdAndToUserIdAndStatus(a, b, ACCEPTED) ||
                        friendRepository.existsByFromUserIdAndToUserIdAndStatus(b, a, ACCEPTED);

        if (alreadyFriend) {
            throw new CustomException(ErrorCode.FRIEND_ALREADY_ACCEPTED);
        }
    }
}