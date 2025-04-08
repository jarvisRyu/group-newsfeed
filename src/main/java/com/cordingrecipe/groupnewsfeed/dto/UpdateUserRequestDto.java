package com.cordingrecipe.groupnewsfeed.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateUserRequestDto {

    @NotBlank
    @Size(max = 50)
    private final String username;
    @NotBlank
    @Size(max = 500)
    private final String email;
    @NotBlank
    @Size(max = 500)
    private final String password;
}
