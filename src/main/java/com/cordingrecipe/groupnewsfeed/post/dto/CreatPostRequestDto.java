package com.cordingrecipe.groupnewsfeed.post.dto;

import lombok.Getter;

@Getter
public class CreatPostRequestDto {

    private final String title;

    private final String contents;

    public CreatPostRequestDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
