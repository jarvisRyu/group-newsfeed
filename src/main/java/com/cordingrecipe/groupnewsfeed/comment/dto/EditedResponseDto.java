package com.cordingrecipe.groupnewsfeed.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditedResponseDto {

    private String updatedComment;

    public EditedResponseDto(String updatedComment){
        this.updatedComment = updatedComment;
    }

}
