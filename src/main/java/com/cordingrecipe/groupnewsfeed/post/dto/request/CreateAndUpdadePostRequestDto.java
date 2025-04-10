package com.cordingrecipe.groupnewsfeed.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateAndUpdadePostRequestDto {

    @NotBlank(message = "내용은 필수입니다.")
    private final String contents;

    public CreateAndUpdadePostRequestDto(String contents) {

        this.contents = contents;
    }

}
