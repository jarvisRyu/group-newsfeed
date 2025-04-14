package com.cordingrecipe.groupnewsfeed.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentRequestDto {

    @NotBlank(message = "댓글을 입력해주세요.")
    private String commentContent;

}
