package com.cordingrecipe.groupnewsfeed.friend.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class CreateFriendRequestDto {

    private final Long toUserId;

}

