package com.cordingrecipe.groupnewsfeed.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SignUpRequestDto {

    @Size(max=10, message = "10글자를 초과하지 마십시오.")
    @NotBlank
    private final String username;
    @Email(message = "이메일 형식으로 작성하십시오.")
    @NotBlank
    private final String email;
    @Size(min=8, message = "비밀번호는 최소 8자 이상입니다.")
    @NotBlank
    private final String password;


}
