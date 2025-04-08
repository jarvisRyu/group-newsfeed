package com.cordingrecipe.groupnewsfeed.post.controller;

import com.cordingrecipe.groupnewsfeed.post.dto.CreatPostRequestDto;
import com.cordingrecipe.groupnewsfeed.post.dto.CreatePostResponseDto;
import com.cordingrecipe.groupnewsfeed.post.dto.UpdatePostResponseDto;
import com.cordingrecipe.groupnewsfeed.post.service.Postservice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final Postservice postService;

    public PostController(Postservice postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<CreatePostResponseDto> savePost(@RequestBody CreatPostRequestDto requestDto) {

        CreatePostResponseDto createPostResponseDto =
                postService.savePost(
                        requestDto.getTitle(),
                        requestDto.getContents()
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

    @PutMapping("/{id}")
    public ResponseEntity<UpdatePostResponseDto> updatePost(
            @PathVariable Long id,
            @RequestBody CreatPostRequestDto requestDto
    ) {
        UpdatePostResponseDto updatePostResponseDto =
                postService.updatePost(
                        id,
                        requestDto.getTitle(),
                        requestDto.getContents()
                );

        return new ResponseEntity<>(updatePostResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostById(@PathVariable Long id) {

        postService.deletePostById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
