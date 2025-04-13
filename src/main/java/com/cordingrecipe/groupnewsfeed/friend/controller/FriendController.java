package com.cordingrecipe.groupnewsfeed.friend.controller;

import com.cordingrecipe.groupnewsfeed.common.constant.Const;
import com.cordingrecipe.groupnewsfeed.friend.dto.CreateFriendRequestDto;
import com.cordingrecipe.groupnewsfeed.friend.dto.CreateFriendResponseDto;
import com.cordingrecipe.groupnewsfeed.friend.dto.FriendResponseDto;
import com.cordingrecipe.groupnewsfeed.friend.dto.FriendViewDto;
import com.cordingrecipe.groupnewsfeed.friend.service.FriendService;
import com.cordingrecipe.groupnewsfeed.user.dto.UserLoginResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @PostMapping
    public ResponseEntity<CreateFriendResponseDto> createFriend(@RequestBody CreateFriendRequestDto requestDto,  @SessionAttribute(name = "LOGIN_USER") UserLoginResponseDto loginUser) {

        CreateFriendResponseDto createFriendResponseDto =
                friendService.createFriend(loginUser.getId(), requestDto);
        return ResponseEntity.ok(createFriendResponseDto);
    }

    @PatchMapping("requests/{id}/accept")
    public ResponseEntity<FriendResponseDto> acceptFriendRequest(@PathVariable Long id, @SessionAttribute(name = "LOGIN_USER")UserLoginResponseDto loginUser) {

        FriendResponseDto friendResponseDto =
                friendService.acceptFriendRequest(loginUser.getId(), id);

        return ResponseEntity.ok(friendResponseDto);
    }

    //requests/{id} → 친구 요청이라는 자원
    //PATCH → 그 자원의 상태를 변경하겠다는 행위,
    @PatchMapping("requests/{id}/reject")
    public ResponseEntity<FriendResponseDto> declineFriendRequest(@PathVariable Long id, @SessionAttribute(name = "LOGIN_USER")UserLoginResponseDto loginUser) {

        FriendResponseDto friendResponseDto = friendService.rejectFriendRequest(loginUser.getId(), id);
        return ResponseEntity.ok(friendResponseDto);
    }

    @GetMapping("/received")
    public ResponseEntity<List<FriendViewDto>> getFriend(@SessionAttribute(name = "LOGIN_USER")UserLoginResponseDto loginUser) {


        List<FriendViewDto> friendList = friendService.getReceivedRequests(loginUser.getId());
        return ResponseEntity.ok(friendList);
    }

    @GetMapping("/requests/from/received")
    public ResponseEntity<List<FriendResponseDto>> checkUserSentRequest(@SessionAttribute(name = "LOGIN_USER")UserLoginResponseDto loginUser) {

        List<FriendResponseDto> friendList = friendService.getPendingFriendRequests(loginUser.getId());
        return ResponseEntity.ok(friendList);

    }

    @GetMapping("/relations")
    public ResponseEntity<FriendViewDto> getFriendRelation(
            @RequestParam("friendId") Long friendId,
            @SessionAttribute(Const.LOGIN_USER) UserLoginResponseDto loginUser) {

        FriendViewDto response = friendService.findFriend(loginUser.getId(), friendId);
        return ResponseEntity.ok(response);
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<FriendResponseDto> deleteFriends(@PathVariable Long id, @SessionAttribute(name = "LOGIN_USER")UserLoginResponseDto loginUser) {

        friendService.deleteFriend(loginUser.getId(), id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}

