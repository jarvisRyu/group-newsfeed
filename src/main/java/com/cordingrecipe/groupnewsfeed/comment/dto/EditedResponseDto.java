package com.cordingrecipe.groupnewsfeed.comment.dto;

import com.cordingrecipe.groupnewsfeed.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
public class EditedResponseDto {

    private String updatedComment;

    public EditedResponseDto(String updatedComment){
        this.updatedComment = updatedComment;
    }

    public static EditedResponseDto toDto(Comment comment){
        return new EditedResponseDto(
                comment.getCommentContent()
                );
    }
}
