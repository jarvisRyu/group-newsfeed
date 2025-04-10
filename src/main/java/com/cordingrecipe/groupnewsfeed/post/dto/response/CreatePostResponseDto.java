package com.cordingrecipe.groupnewsfeed.post.dto.response;

import com.cordingrecipe.groupnewsfeed.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreatePostResponseDto {

    private final Long id;

    private final String username;

    private final String contents;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    public CreatePostResponseDto(Long id, String username, String contents, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.contents = contents;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CreatePostResponseDto toDto(Post post) {
        return new CreatePostResponseDto(
                post.getId(),
                post.getUser().getUsername(),
                post.getContents(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
