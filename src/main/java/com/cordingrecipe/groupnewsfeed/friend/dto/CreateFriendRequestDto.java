package com.cordingrecipe.groupnewsfeed.friend.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class CreateFriendRequestDto {

    //requesterId는 session에 처리된 정보로 사용할 것임.

    //친구요청하고 싶은 상대
    private final Long receiverId;

    public CreateFriendRequestDto(Long receiverId) {
        this.receiverId = receiverId;
    }
}
