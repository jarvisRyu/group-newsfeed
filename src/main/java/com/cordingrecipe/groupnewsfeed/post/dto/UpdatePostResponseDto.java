package com.cordingrecipe.groupnewsfeed.post.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdatePostResponseDto {

    private final Long id;

    private final String title;

    private final String contents;

    private final LocalDateTime updatedAt;

    public UpdatePostResponseDto(Long id, String title, String contents, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.updatedAt = updatedAt;
    }
}
