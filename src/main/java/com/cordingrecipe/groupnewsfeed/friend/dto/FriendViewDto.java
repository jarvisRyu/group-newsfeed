package com.cordingrecipe.groupnewsfeed.friend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class FriendViewDto {

    private final Long friendId;
    private final String friendName;

    public FriendViewDto(Long friendId, String friendName) {
        this.friendId = friendId;
        this.friendName = friendName;
    }
}
