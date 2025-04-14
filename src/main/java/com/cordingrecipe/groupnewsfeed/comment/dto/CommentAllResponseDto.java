package com.cordingrecipe.groupnewsfeed.comment.dto;


import com.cordingrecipe.groupnewsfeed.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CommentAllResponseDto {

    private Long commentId;
    private String commentContent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public static CommentAllResponseDto toDto(Comment comment) {
        return new CommentAllResponseDto(
                comment.getId(),
                comment.getCommentContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }

}

