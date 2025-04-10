package com.cordingrecipe.groupnewsfeed.post.dto.request;

import lombok.Getter;

@Getter
public class CreateAndUpdadePostRequestDto {

    private final String title;

    private final String contents;

    public CreateAndUpdadePostRequestDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
