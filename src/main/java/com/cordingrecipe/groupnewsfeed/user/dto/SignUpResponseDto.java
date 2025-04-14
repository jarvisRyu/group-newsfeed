package com.cordingrecipe.groupnewsfeed.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpResponseDto {

    private final Long id;
    private final String username;
    private final String email;
}
