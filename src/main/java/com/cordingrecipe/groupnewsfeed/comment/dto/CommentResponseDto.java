package com.cordingrecipe.groupnewsfeed.comment.dto;

import com.cordingrecipe.groupnewsfeed.comment.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private Long id;

    private String commentContent;

    public CommentResponseDto(Long id ,String updatedCommentContent){
        this.id = id;
        this.commentContent=updatedCommentContent;
    }

    public static CommentResponseDto toDto(Comment comment){
        return new CommentResponseDto(
                comment.getId(),
                comment.getCommentContent()
        );
    }
}
