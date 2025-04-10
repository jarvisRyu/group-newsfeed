package com.cordingrecipe.groupnewsfeed.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateAndUpdadePostRequestDto {

    @NotBlank(message = "제목은 필수입니다.")
    private final String title;

    @NotBlank(message = "내용은 필수입니다.")
    private final String contents;

    public CreateAndUpdadePostRequestDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
