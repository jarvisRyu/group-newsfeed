package com.cordingrecipe.groupnewsfeed.friends.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class CreateFriendResponseDto {

    private final Long requestId;

    private final Long receiveId;


}
