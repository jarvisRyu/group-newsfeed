package com.cordingrecipe.groupnewsfeed.user.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateIntroductionRequestDto {

    @Size(max=200,message = "200자를 넘을 수 없습니다.")
    private final String introduction;

}
