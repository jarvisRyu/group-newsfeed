package com.cordingrecipe.groupnewsfeed.post.controller;

import com.cordingrecipe.groupnewsfeed.post.dto.request.CreateAndUpdadePostRequestDto;
import com.cordingrecipe.groupnewsfeed.post.dto.response.CreatePostResponseDto;
import com.cordingrecipe.groupnewsfeed.post.dto.response.UpdatePostResponseDto;
import com.cordingrecipe.groupnewsfeed.post.service.PostService;
import com.cordingrecipe.groupnewsfeed.user.dto.UserLoginResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/boards")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<CreatePostResponseDto> savePost(@RequestBody CreateAndUpdadePostRequestDto requestDto, HttpServletRequest request) {

        // 로그인 인가 세션에서 사용자 ID 가져오기
        HttpSession session = request.getSession(false); // 기존 세션 가져오기
        UserLoginResponseDto loginUserId = (UserLoginResponseDto) session.getAttribute("LOGIN_USER");
        Long userId = loginUserId.getId(); // 세션에서 로그인된 사용자 ID 꺼내기

        CreatePostResponseDto createPostResponseDto =
                postService.savePost(
                        requestDto.getContents(),
                        userId
                );

        return new ResponseEntity<>(createPostResponseDto, HttpStatus.OK);
    }

    // 페이징 기능으로 리펙토리
    @GetMapping("/newsfeed")
    public ResponseEntity<Page<CreatePostResponseDto>> findAllPost(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Page<CreatePostResponseDto> allPostPage = postService.findAllPost(page, size);

        return new ResponseEntity<>(allPostPage, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreatePostResponseDto> findById(@PathVariable Long id) {
        CreatePostResponseDto createPostResponseDto = postService.findById(id);

        return new ResponseEntity<>(createPostResponseDto, HttpStatus.OK);
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<UpdatePostResponseDto> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody CreateAndUpdadePostRequestDto requestDto,
            HttpServletRequest request
    ) {

        // 로그인 인가 세션에서 사용자 ID 가져오기
        HttpSession session = request.getSession(false); // 기존 세션 가져오기
        UserLoginResponseDto loginUserId = (UserLoginResponseDto) session.getAttribute("LOGIN_USER");
        Long userId = loginUserId.getId(); // 세션에서 로그인된 사용자 ID 꺼내기

        UpdatePostResponseDto updatePostResponseDto =
                postService.updatePost(id,
                        requestDto.getContents(),
                        userId
                );

        return new ResponseEntity<>(updatePostResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostById(
            @PathVariable Long id,
            HttpServletRequest request) {

        // 로그인 인가 세션에서 사용자 ID 가져오기
        HttpSession session = request.getSession(false); // 기존 세션 가져오기
        UserLoginResponseDto loginUserId = (UserLoginResponseDto) session.getAttribute("LOGIN_USER");
        Long userId = loginUserId.getId(); // 세션에서 로그인된 사용자 ID 꺼내기

        postService.deletePostById(id, userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
