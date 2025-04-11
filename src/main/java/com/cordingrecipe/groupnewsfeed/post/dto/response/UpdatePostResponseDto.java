package com.cordingrecipe.groupnewsfeed.post.dto.response;

import com.cordingrecipe.groupnewsfeed.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdatePostResponseDto {

    private final Long id;

    private final String contents;

    private final LocalDateTime updatedAt;

    public UpdatePostResponseDto(Long id, String contents, LocalDateTime updatedAt) {
        this.id = id;
        this.contents = contents;
        this.updatedAt = updatedAt;
    }

    public static UpdatePostResponseDto from(Post post) {
        return new UpdatePostResponseDto(
                post.getId(),
                post.getContents(),
                post.getUpdatedAt()
        );
    }
}
