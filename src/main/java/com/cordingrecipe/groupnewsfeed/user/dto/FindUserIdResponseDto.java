package com.cordingrecipe.groupnewsfeed.user.dto;
import com.cordingrecipe.groupnewsfeed.user.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FindUserIdResponseDto {

        private final Long id;
        private final String username;
        private final String email;
        private final String introduction;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private final LocalDateTime createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private final LocalDateTime updatedAt;


       public static FindUserIdResponseDto toDto(User user){
            return new FindUserIdResponseDto(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getIntroduction(),
                    user.getCreatedAt(),
                    user.getUpdatedAt()
            );
        }
}
