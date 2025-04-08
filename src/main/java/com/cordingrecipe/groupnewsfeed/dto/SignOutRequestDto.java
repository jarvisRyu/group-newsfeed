package com.cordingrecipe.groupnewsfeed.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignOutRequestDto {
    private final String password;
}
