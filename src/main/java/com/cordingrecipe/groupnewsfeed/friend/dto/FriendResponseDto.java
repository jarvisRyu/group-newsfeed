package com.cordingrecipe.groupnewsfeed.friend.dto;

import com.cordingrecipe.groupnewsfeed.friend.entity.Friend;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class FriendResponseDto {

    private final Long receiverId; //toUserid
    private final Long senderId;
    private final String status;

    public FriendResponseDto(Friend friend) {
        this.receiverId = friend.getToUser().getId();
        this.senderId = friend.getFromUser().getId();
        this.status = friend.getStatus().name();
    }


}
