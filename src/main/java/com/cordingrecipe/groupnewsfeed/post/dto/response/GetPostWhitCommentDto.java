package com.cordingrecipe.groupnewsfeed.post.dto.response;

import com.cordingrecipe.groupnewsfeed.comment.dto.CommentAllResponseDto;
import com.cordingrecipe.groupnewsfeed.comment.dto.CommentResponseDto;
import com.cordingrecipe.groupnewsfeed.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class GetPostWhitCommentDto {

    private final Long id;

    private final String username;

    private final String contents;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    private final List<CommentAllResponseDto> comments;

    public GetPostWhitCommentDto(Long id, String username, String contents, LocalDateTime createdAt, LocalDateTime updatedAt, List<CommentAllResponseDto> comments) {
        this.id = id;
        this.username = username;
        this.contents = contents;
        this.comments = comments;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static GetPostWhitCommentDto from(Post post, List<CommentAllResponseDto> comments) {
        return new GetPostWhitCommentDto(
                post.getId(),
                post.getUser().getUsername(),
                post.getContents(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                comments
        );
    }
}
