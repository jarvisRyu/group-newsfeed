package com.cordingrecipe.groupnewsfeed.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserLoginRequestDto {

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",message = "유효한 이메일 형식이 아닙니다.")
    private final String email;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private final String password;




}
