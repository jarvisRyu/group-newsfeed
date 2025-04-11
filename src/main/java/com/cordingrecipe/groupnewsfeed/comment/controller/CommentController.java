package com.cordingrecipe.groupnewsfeed.comment.controller;

import com.cordingrecipe.groupnewsfeed.comment.dto.*;
import com.cordingrecipe.groupnewsfeed.comment.service.CommentService;
import com.cordingrecipe.groupnewsfeed.user.dto.UserLoginResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/boards/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CommentResponseDto> postComments (@PathVariable Long postId,
                                                     @SessionAttribute ("LOGIN_USER") UserLoginResponseDto loginUser,
                                                     @Valid @RequestBody CommentRequestDto dto){
        Long userId = loginUser.getId(); // 기존 세션 가져오기
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
    ResponseEntity<EditedResponseDto> updateComment(@PathVariable Long postId,
                                                    @PathVariable Long commentId,
                                                    @Valid @RequestBody EditCommentRequestDto editCommentRequestDto,
                                                    @SessionAttribute ("LOGIN_USER") UserLoginResponseDto loginUser) {
        Long userId = loginUser.getId();
        EditedResponseDto dto = commentService.updateComment(postId, commentId, editCommentRequestDto.getWishComment(), userId);
        return ResponseEntity.ok(dto);
    }

    // 특정 댓글 삭제
    @DeleteMapping(value = "/{commentId}", produces= MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> delete(@PathVariable Long postId,
                                  @PathVariable Long commentId,
                                  @SessionAttribute ("LOGIN_USER") UserLoginResponseDto loginUser){
        Long userId = loginUser.getId();
        commentService.delete(postId, commentId, userId);
        return ResponseEntity.ok("선택하신 댓글 삭제가 완료되었습니다.");
    }
}
