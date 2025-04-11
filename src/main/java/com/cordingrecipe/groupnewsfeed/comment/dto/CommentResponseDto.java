package com.cordingrecipe.groupnewsfeed.comment.dto;

import com.cordingrecipe.groupnewsfeed.comment.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private String commentContent;

    public CommentResponseDto(String updatedCommentContent){
        this.commentContent=updatedCommentContent;
    }

    public static CommentResponseDto toDto(Comment comment){
        return new CommentResponseDto(
                comment.getCommentContent()
        );
    }
}
