package com.cordingrecipe.groupnewsfeed.comment.service;

import com.cordingrecipe.groupnewsfeed.comment.dto.CommentResponseDto;
import com.cordingrecipe.groupnewsfeed.comment.entity.Comment;
import com.cordingrecipe.groupnewsfeed.comment.repository.CommentRepository;
import com.cordingrecipe.groupnewsfeed.post.entity.Post;
import com.cordingrecipe.groupnewsfeed.user.entity.User;
import com.cordingrecipe.groupnewsfeed.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PasswordEncorder passwordEncorder;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentResponseDto postComments(Long postId, Long userId, String commentContent) {
        User user = userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("해당하는 유저가 존재하지 않습니다."));
        Post post = postRepositoy.findById(postId).orElseThrow(()-> new IllegalArgumentException("해당하는 게시글이 존재하지 않습니다."));
        if(!post.getUser().getId().equals(user.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }
        Comment comment = new Comment(user, post, commentContent);
        commentRepository.save(comment);
        return new CommentResponseDto(comment.getCommentContent());
    }

}
