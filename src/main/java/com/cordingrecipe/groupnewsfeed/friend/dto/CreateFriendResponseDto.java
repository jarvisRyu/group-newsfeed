package com.cordingrecipe.groupnewsfeed.friend.dto;

import com.cordingrecipe.groupnewsfeed.friend.entity.Friends;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class CreateFriendResponseDto {

    private final Long receiverId; //toUserid
    private final Long senderId;
    private final String status;

    //User객체에서 받아와서 User id만 꺼낼
    public CreateFriendResponseDto(Friends friend) {
        this.receiverId = friend.getToUser().getId();
        this.senderId = friend.getFromUser().getId();
        this.status = friend.getStatus().name();
    }


}
