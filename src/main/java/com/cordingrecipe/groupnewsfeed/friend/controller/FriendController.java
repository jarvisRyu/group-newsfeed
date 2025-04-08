package com.cordingrecipe.groupnewsfeed.friend.controller;

import com.cordingrecipe.groupnewsfeed.friend.service.FriendService;
import com.cordingrecipe.groupnewsfeed.friend.dto.CreateFriendRequestDto;
import com.cordingrecipe.groupnewsfeed.friend.dto.CreateFriendResponseDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @PostMapping
    public ResponseEntity<CreateFriendResponseDto> createFriend(@RequestBody CreateFriendRequestDto requestDto, HttpSession session) {
        CreateFriendResponseDto createFriendResponseDto =
                friendService.createFriend(requestDto, session);

        return ResponseEntity.ok(createFriendResponseDto);

    }

    @PatchMapping("requests/{id}/accept")
    public ResponseEntity<CreateFriendResponseDto> acceptFriendRequest(@PathVariable Long id, HttpSession session) {
        CreateFriendResponseDto createFriendResponseDto =
                friendService.acceptFriendRequest(id, session);

        return ResponseEntity.ok(createFriendResponseDto);
    }

    @PatchMapping("requests/{id}/reject")
    public ResponseEntity<CreateFriendResponseDto> declineFriendRequest(@PathVariable Long id, HttpSession session) {
        CreateFriendResponseDto createFriendResponseDto =
                friendService.rejectFriendRequest(id, session);

        return ResponseEntity.ok(createFriendResponseDto);
    }
}
