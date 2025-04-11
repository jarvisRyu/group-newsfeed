package com.cordingrecipe.groupnewsfeed.friend.controller;

import com.cordingrecipe.groupnewsfeed.friend.dto.CreateFriendRequestDto;
import com.cordingrecipe.groupnewsfeed.friend.dto.CreateFriendResponseDto;
import com.cordingrecipe.groupnewsfeed.friend.service.FriendService;
import com.cordingrecipe.groupnewsfeed.user.dto.UserLoginResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @PostMapping
    public ResponseEntity<CreateFriendResponseDto> createFriend(@RequestBody CreateFriendRequestDto requestDto,  @SessionAttribute(name = "LOGIN_USER", required = false) UserLoginResponseDto loginUser) {
        if (loginUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        CreateFriendResponseDto createFriendResponseDto =
                friendService.createFriend(loginUser.getId(), requestDto);
        return ResponseEntity.ok(createFriendResponseDto);
    }

    @PatchMapping("requests/{id}/accept")
    public ResponseEntity<CreateFriendResponseDto> acceptFriendRequest(@PathVariable Long id, @SessionAttribute(name = "LOGIN_USER", required = false)UserLoginResponseDto loginUser) {
        if (loginUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        CreateFriendResponseDto createFriendResponseDto =
                friendService.acceptFriendRequest(loginUser.getId(), id);

        return ResponseEntity.ok(createFriendResponseDto);
    }

    //requests/{id} → 친구 요청이라는 자원
    //PATCH → 그 자원의 상태를 변경하겠다는 행위,
    @PatchMapping("requests/{id}/reject")
    public ResponseEntity<CreateFriendResponseDto> declineFriendRequest(@PathVariable Long id, @SessionAttribute(name = "LOGIN_USER", required = false)UserLoginResponseDto loginUser) {
        if (loginUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        CreateFriendResponseDto createFriendResponseDto = friendService.rejectFriendRequest(loginUser.getId(), id);
        return ResponseEntity.ok(createFriendResponseDto);
    }

    @GetMapping("/received")
    public ResponseEntity<List<CreateFriendResponseDto>> getFriend(@SessionAttribute(name = "LOGIN_USER", required = false)UserLoginResponseDto loginUser) {
        if (loginUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        List<CreateFriendResponseDto> friendList = friendService.getReceivedRequests(loginUser.getId());
        return ResponseEntity.ok(friendList);
    }

    @GetMapping("/requests/from/received")
    public ResponseEntity<List<CreateFriendResponseDto>> checkUserSentRequest(@SessionAttribute(name = "LOGIN_USER", required = false)UserLoginResponseDto loginUser) {
        if (loginUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        List<CreateFriendResponseDto> friendList = friendService.getPendingFriendRequests(loginUser.getId());
        return ResponseEntity.ok(friendList);

    }

    @GetMapping("/relations/between")
    public ResponseEntity<CreateFriendResponseDto> findFriends(@RequestParam Long fromUserId, @RequestParam Long toUserId,@SessionAttribute(name = "LOGIN_USER", required = false)UserLoginResponseDto loginUser) {
        if (loginUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        CreateFriendResponseDto createFriendResponseDto = friendService.findFriends(fromUserId,toUserId,loginUser.getId());
        return ResponseEntity.ok(createFriendResponseDto);
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CreateFriendResponseDto> deleteFriends(@PathVariable Long id, @SessionAttribute(name = "LOGIN_USER", required = false)UserLoginResponseDto loginUser) {
        if (loginUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        friendService.deleteFriend(loginUser.getId(), id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}

