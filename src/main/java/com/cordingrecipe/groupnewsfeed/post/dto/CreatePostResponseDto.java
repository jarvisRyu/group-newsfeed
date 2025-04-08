package com.cordingrecipe.groupnewsfeed.post.dto;

import com.cordingrecipe.groupnewsfeed.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreatePostResponseDto {

    private final Long id;

    private final String username;

    private final String title;

    private final String contents;

    private final LocalDateTime createdAt;

    public CreatePostResponseDto(Long id, String username, String title, String contents, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.contents = contents;
        this.createdAt = createdAt;
    }

    public static CreatePostResponseDto toDto(Post post){
        return new CreatePostResponseDto(post.getId(), post.getUser().getUserName(), post.getTitle(), post.getContents(), post.getCreatedAt());
    }
}
