package com.cordingrecipe.groupnewsfeed.comment.service;

import com.cordingrecipe.groupnewsfeed.comment.dto.CommentAllResponseDto;
import com.cordingrecipe.groupnewsfeed.comment.dto.CommentResponseDto;
import com.cordingrecipe.groupnewsfeed.comment.dto.EditedResponseDto;
import com.cordingrecipe.groupnewsfeed.comment.entity.Comment;
import com.cordingrecipe.groupnewsfeed.comment.repository.CommentRepository;
import com.cordingrecipe.groupnewsfeed.common.advice.CustomException;
import com.cordingrecipe.groupnewsfeed.common.advice.ErrorCode;
import com.cordingrecipe.groupnewsfeed.post.entity.Post;
import com.cordingrecipe.groupnewsfeed.post.repository.PostRepository;
import com.cordingrecipe.groupnewsfeed.user.entity.User;
import com.cordingrecipe.groupnewsfeed.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentResponseDto postComments(Long postId, Long userId, String commentContent) {

        User user = userRepository.findByIdOrElseThrow(userId);
        Post post = postRepository.findByIdOrElseThrow(postId);

        // 댓글 생성
        Comment comment = Comment.create(user,post,commentContent);
        // 댓글 저장
        commentRepository.save(comment);
        return CommentResponseDto.toDto(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentAllResponseDto> getComments(Long postId) {
        List<Comment> comments = commentRepository.findByPostIdOrderByUpdatedAtDesc(postId);
        // 찾은 댓글들을 Stream을 통하여 List<CommentAllResponseDto>로 변환하여 return
        return comments.stream()
                .map(CommentAllResponseDto::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EditedResponseDto updateComment(Long postId, Long commentId, String wishComment, Long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);
        Post post = postRepository.findByIdOrElseThrow(postId);
        Comment comment = commentRepository.findByIdOrElseThrow(commentId);

        boolean isPostWriter = user.hasDeleteRole(userId, post); // 게시글 작성자인지 확인
        boolean isCommentWriter = comment.hasDeleteRole(commentId, userId); // 댓글 작성자인지 확인

        // 댓글 작성자 및 게시글 작성자 본인이 아닐시 예외 발생
        if(!(isPostWriter || isCommentWriter)){
            throw new CustomException(ErrorCode.COMMENT_ACCESS_DENIED);
        }

        // 수정된 댓글을 업데이트
        comment.updateComment(wishComment);
        return EditedResponseDto.toDto(comment);
    }

    @Transactional
    public void delete(Long postId, Long commentId, Long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);
        Post post = postRepository.findByIdOrElseThrow(postId);
        Comment comment = commentRepository.findByIdOrElseThrow(commentId);

        boolean isPostWriter = user.hasDeleteRole(userId, post); // 게시글 작성자인지 확인
        boolean isCommentWriter = comment.hasDeleteRole(commentId, userId); // 댓글 작성자인지 확인

        // 댓글 작성자 및 게시글 작성자 본인이 아닐시 예외 발생
        if(!(isPostWriter || isCommentWriter)){
            throw new CustomException(ErrorCode.COMMENT_ACCESS_DENIED);
        }

        // 받아옴 id로 조회된 댓글을 삭제
        commentRepository.delete(comment);
    }
}
