package com.cordingrecipe.groupnewsfeed.post.controller;

import com.cordingrecipe.groupnewsfeed.post.dto.CreatePostRequestDto;
import com.cordingrecipe.groupnewsfeed.post.dto.CreatePostResponseDto;
import com.cordingrecipe.groupnewsfeed.post.dto.UpdatePostResponseDto;
import com.cordingrecipe.groupnewsfeed.post.service.PostService;
import com.cordingrecipe.groupnewsfeed.user.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<CreatePostResponseDto> savePost(@RequestBody CreatePostRequestDto requestDto) {

        CreatePostResponseDto createPostResponseDto =
                postService.savePost(
                        requestDto.getTitle(),
                        requestDto.getContents(),
                        new User()
                );

        return new ResponseEntity<>(createPostResponseDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CreatePostResponseDto>> findAllPost() {

        List<CreatePostResponseDto> allPostList = postService.findAllPost();

        return new ResponseEntity<>(allPostList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreatePostResponseDto> findById(@PathVariable Long id) {
        CreatePostResponseDto createPostResponseDto = postService.findById(id);

        return new ResponseEntity<>(createPostResponseDto, HttpStatus.OK);
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<UpdatePostResponseDto> updatePost(
            @PathVariable Long id,
            @RequestBody CreatePostRequestDto requestDto,
            HttpSession session // 세션 추가
    ) throws AccessDeniedException {
        UpdatePostResponseDto updatePostResponseDto =
                postService.updatePost(
                        id,
                        requestDto.getTitle(),
                        requestDto.getContents(),
                        session // 서비스에 세션 전달

                );

        return new ResponseEntity<>(updatePostResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostById(
            @PathVariable Long id,
            @RequestBody HttpSession session) throws AccessDeniedException {

        postService.deletePostById(id, session);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
