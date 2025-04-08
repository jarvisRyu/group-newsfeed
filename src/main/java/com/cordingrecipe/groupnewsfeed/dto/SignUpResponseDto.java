package com.cordingrecipe.groupnewsfeed.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpResponseDto {

    private final Long id;
    private final String username;
    private final String email;
}
