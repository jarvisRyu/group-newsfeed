package com.cordingrecipe.groupnewsfeed.user.dto;

import lombok.Getter;

@Getter
public class UserLoginResponseDto {

    Long id;
    String userName;

    public UserLoginResponseDto(Long id, String userName) {
        this.id = id;
        this.userName = userName;
    }
}
