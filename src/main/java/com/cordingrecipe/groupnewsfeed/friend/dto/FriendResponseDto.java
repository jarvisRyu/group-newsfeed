package com.cordingrecipe.groupnewsfeed.friend.dto;

import com.cordingrecipe.groupnewsfeed.friend.entity.Friend;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class FriendResponseDto {

    private final Long toUserId;
    private final Long fromUserId;
    private final String status;

    public FriendResponseDto(Friend friend) {
        this.fromUserId = friend.getFromUser().getId();
        this.toUserId = friend.getToUser().getId();
        this.status = friend.getStatus().name();
    }
}