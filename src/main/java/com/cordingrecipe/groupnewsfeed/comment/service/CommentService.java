package com.cordingrecipe.groupnewsfeed.comment.service;

import com.cordingrecipe.groupnewsfeed.comment.dto.CommentAllResponseDto;
import com.cordingrecipe.groupnewsfeed.comment.dto.CommentResponseDto;
import com.cordingrecipe.groupnewsfeed.comment.dto.EditedResponseDto;
import com.cordingrecipe.groupnewsfeed.comment.entity.Comment;
import com.cordingrecipe.groupnewsfeed.comment.repository.CommentRepository;
import com.cordingrecipe.groupnewsfeed.common.advice.CustomException;
import com.cordingrecipe.groupnewsfeed.common.advice.ErrorCode;
import com.cordingrecipe.groupnewsfeed.common.util.CommentUtils;
import com.cordingrecipe.groupnewsfeed.post.entity.Post;
import com.cordingrecipe.groupnewsfeed.user.entity.User;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentUtils commentUtils;

    @Transactional
    public CommentResponseDto postComments(Long postId, Long userId, String commentContent) {
        User user = commentUtils.findUserById(userId);
        Post post = commentUtils.findPostById(postId);

        Comment comment = new Comment(user, post, commentContent);
        commentRepository.save(comment);
        return new CommentResponseDto(comment.getCommentContent());
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
        User user = commentUtils.findUserById(userId);
        Post post = commentUtils.findPostById(postId);
        Comment comment = commentUtils.findCommentById(commentId);

        if(!(user.getId().equals(post.getUser().getId()) || comment.getUser().getId().equals(user.getId()))){
            throw new CustomException(ErrorCode.COMMENT_ACCESS_DENIED);
        }

        comment.setCommentContent(wishComment);

        return new EditedResponseDto(comment.getCommentContent());

    }

    @Transactional
    public void delete(Long postId, Long commentId, Long userId) {
        User user = commentUtils.findUserById(userId);
        Post post = commentUtils.findPostById(postId);
        Comment comment = commentUtils.findCommentById(commentId);

        if(!(user.getId().equals(post.getUser().getId()) || comment.getUser().getId().equals(user.getId()))){
            throw new CustomException(ErrorCode.COMMENT_ACCESS_DENIED);
        }

        commentRepository.delete(comment);
    }
}
