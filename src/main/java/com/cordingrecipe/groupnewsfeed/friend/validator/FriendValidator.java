package com.cordingrecipe.groupnewsfeed.friend.validator;

import com.cordingrecipe.groupnewsfeed.common.advice.CustomException;
import com.cordingrecipe.groupnewsfeed.common.advice.ErrorCode;
import com.cordingrecipe.groupnewsfeed.friend.entity.Friends;
import com.cordingrecipe.groupnewsfeed.user.entity.User;
import com.cordingrecipe.groupnewsfeed.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class FriendValidator {

    private final UserRepository userRepository;

    public User validateUserExist(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    } //리포지토리 디폴드메서드

    public void validateReceiver(Long userId, Friends friendRequest) {
        if (!friendRequest.getToUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FRIEND_ACCESS_DENIED);
        }
    }
}