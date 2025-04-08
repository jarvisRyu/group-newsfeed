package com.cordingrecipe.groupnewsfeed.comment.controller;

import com.cordingrecipe.groupnewsfeed.comment.dto.*;
import com.cordingrecipe.groupnewsfeed.comment.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CommentResponseDto> postComments (@PathVariable Long postId, HttpServletRequest request, CommentRequestDto dto){

        HttpSession session = request.getSession(false); // 기존 세션 가져오기
        UserLoginResponseDto loginUser = (UserLoginResponseDto) session.getAttribute("LOGIN_USER");
        Long userId = loginUser.getId(); // 아이디 꺼내기


        CommentResponseDto response = commentService.postComments(postId, userId, dto.getCommentContent());
        return ResponseEntity.ok(response);

    }

}
