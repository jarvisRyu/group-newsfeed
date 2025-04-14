package com.cordingrecipe.groupnewsfeed.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",message = "유효한 이메일 형식이 아닙니다.")
    private final String email;

    @Pattern( regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]:\";'<>?,./]).{8,}$"
            ,message = "비밀번호는 영문, 숫자, 특수문자를 포함하여 8자 이상이어야 합니다.")
    @Size(min = 8)
    @NotBlank
    private final String password;


}
