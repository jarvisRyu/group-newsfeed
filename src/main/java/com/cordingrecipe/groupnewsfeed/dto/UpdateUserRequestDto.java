package com.cordingrecipe.groupnewsfeed.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateUserRequestDto {

    @NotBlank
    @Size(max = 10, message = "이름은 10글자를 초과할 수 없습니다.")
    private final String username;
    @NotBlank
    @Email(message = "이메일 형식이 아닙니다.")
    private final String email;
    @NotBlank
    @Size(min = 8, message =" 비밀번호는 8자 이상이어야 합니다." )
    private final String password;
}
