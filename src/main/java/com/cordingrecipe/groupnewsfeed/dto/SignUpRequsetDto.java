package com.cordingrecipe.groupnewsfeed.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter
public class SignUpRequsetDto {

    private final String username;
    private final String email;
    private final String password;
}
