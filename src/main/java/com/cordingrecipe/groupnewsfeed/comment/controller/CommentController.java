package com.cordingrecipe.groupnewsfeed.comment.controller;

import com.cordingrecipe.groupnewsfeed.comment.dto.*;
import com.cordingrecipe.groupnewsfeed.comment.service.CommentService;
import com.cordingrecipe.groupnewsfeed.user.dto.UserLoginResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CommentResponseDto> postComments (@PathVariable Long postId, HttpServletRequest request, CommentRequestDto dto){

        HttpSession session = request.getSession(false); // 기존 세션 가져오기
        UserLoginResponseDto loginUser = (UserLoginResponseDto) session.getAttribute("LOGIN_USER");
        Long userId = loginUser.getId(); // 아이디 꺼내기


        CommentResponseDto response = commentService.postComments(postId, userId, dto.getCommentContent());
        return ResponseEntity.ok(response);

    }

    // 게시물 댓글 조회
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommentAllResponseDto>> getComments(@PathVariable Long postId) {

        List<CommentAllResponseDto> comments = commentService.getComments(postId);
        return ResponseEntity.ok(comments);

    }

    // 특정 댓글 수정
    @PutMapping(value = "/{commentId}",produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<EditedResponseDto> updateComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody EditCommentRequestDto editCommentRequestDto, HttpServletRequest request) {

        HttpSession session = request.getSession(false); // 기존 세션 가져오기
        UserLoginResponseDto loginUser = (UserLoginResponseDto) session.getAttribute("LOGIN_USER");
        Long userId = loginUser.getId();

        EditedResponseDto dto = commentService.updateComment(postId, commentId, editCommentRequestDto.getWishComment(), userId);

        return ResponseEntity.ok(dto);

    }

    // 특정 댓글 삭제
    @DeleteMapping(value = "/{commentId}", produces= MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> delete(@PathVariable Long postId, @PathVariable Long commentId, HttpServletRequest request){

        HttpSession session = request.getSession(false); // 기존 세션 가져오기
        UserLoginResponseDto loginUser = (UserLoginResponseDto) session.getAttribute("LOGIN_USER");
        Long userId = loginUser.getId();

        commentService.delete(postId, commentId, userId);

        return ResponseEntity.ok("삭제가 완료되었습니다.");

    }
}
