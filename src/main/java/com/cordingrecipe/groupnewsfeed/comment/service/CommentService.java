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

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));;
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        Comment comment = Comment.create(user,post,commentContent);
        commentRepository.save(comment);
        return CommentResponseDto.toDto(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentAllResponseDto> getComments(Long postId) {
        List<Comment> comments = commentRepository.findByPostIdOrderByUpdatedAtDesc(postId);
        return comments.stream()
                .map(CommentAllResponseDto::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EditedResponseDto updateComment(Long postId, Long commentId, String wishComment, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if(!(user.getId().equals(post.getUser().getId()) || comment.getUser().getId().equals(user.getId()))){
            throw new CustomException(ErrorCode.COMMENT_ACCESS_DENIED);
        }

        comment.updateComment(wishComment);
        return EditedResponseDto.toDto(comment);
    }

    @Transactional
    public void delete(Long postId, Long commentId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if(!(user.getId().equals(post.getUser().getId()) || comment.getUser().getId().equals(user.getId()))){
            throw new CustomException(ErrorCode.COMMENT_ACCESS_DENIED);
        }

        commentRepository.delete(comment);
    }
}
