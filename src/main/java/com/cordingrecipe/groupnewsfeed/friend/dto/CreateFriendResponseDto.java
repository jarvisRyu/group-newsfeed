package com.cordingrecipe.groupnewsfeed.friend.dto;

import com.cordingrecipe.groupnewsfeed.friend.entity.Friend;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(force = true)
public class CreateFriendResponseDto {

    private final Long receiverId; //toUserid
    private final Long senderId;
    private final String status;
    private LocalDateTime createdAt;


    //User객체에서 받아와서 User id만 꺼낼
    public CreateFriendResponseDto(Friend friend) {
        this.receiverId = friend.getToUser().getId();
        this.senderId = friend.getFromUser().getId();
        this.status = friend.getStatus().name();
        this.createdAt = friend.getCreatedAt();
    }



}
