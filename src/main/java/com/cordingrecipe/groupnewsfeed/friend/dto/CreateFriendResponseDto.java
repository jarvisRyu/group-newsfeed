package com.cordingrecipe.groupnewsfeed.friend.dto;

import com.cordingrecipe.groupnewsfeed.friend.entity.Friend;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(force = true)
public class CreateFriendResponseDto {

    private final Long toUserId;
    private final Long fromUserId; //toUserid
    private final String status;
    private LocalDateTime createdAt;


    //User객체에서 받아와서 User id만 꺼낼
    public CreateFriendResponseDto(Friend friend) {
        this.toUserId = friend.getToUser().getId();
        this.fromUserId = friend.getFromUser().getId();
        this.status = friend.getStatus().name();
        this.createdAt = friend.getCreatedAt();
    }



}
//수정완료