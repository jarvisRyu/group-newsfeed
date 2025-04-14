package com.cordingrecipe.groupnewsfeed.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class EditCommentRequestDto {

    @NotBlank(message = "새로운 댓글을 입력해주세요.")
    private String wishComment;
}
