package com.cordingrecipe.groupnewsfeed.comment.dto;

import lombok.Getter;

@Getter
public class CommentResponseDto {

    private String commentContent;

    public CommentResponseDto(String updatedCommentContent){
        this.commentContent=updatedCommentContent;
    }
}
