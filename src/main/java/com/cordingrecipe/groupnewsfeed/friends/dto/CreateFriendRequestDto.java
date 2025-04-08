package com.cordingrecipe.groupnewsfeed.friends.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class CreateFriendRequestDto {
    private final Long receiveId;

    public CreateFriendRequestDto(Long receiveId) {
        this.receiveId = receiveId;
    }
}
