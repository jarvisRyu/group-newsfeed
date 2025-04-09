package com.cordingrecipe.groupnewsfeed.comment.service;

import com.cordingrecipe.groupnewsfeed.comment.dto.CommentAllResponseDto;
import com.cordingrecipe.groupnewsfeed.comment.dto.CommentResponseDto;
import com.cordingrecipe.groupnewsfeed.comment.dto.EditedResponseDto;
import com.cordingrecipe.groupnewsfeed.comment.entity.Comment;
import com.cordingrecipe.groupnewsfeed.comment.repository.CommentRepository;
import com.cordingrecipe.groupnewsfeed.post.entity.Post;
import com.cordingrecipe.groupnewsfeed.post.repository.PostRepository;
import com.cordingrecipe.groupnewsfeed.user.entity.User;
import com.cordingrecipe.groupnewsfeed.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
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

    @Transactional(readOnly = true)
    public List<CommentAllResponseDto> getComments(Long postId) {
        return postRepository.findById(postId)
                .map(post -> post.getComments()) // 댓글 목록 가져오기
                .stream() // 댓글 목록을 스트림으로 변환
                .flatMap(Collection::stream) // 댓글 리스트를 스트림으로 풀기
                .map(CommentAllResponseDto::toDto) // 각 댓글을 CommentAllResponseDto로 변환
                .collect(Collectors.toList()); // 최종 리스트 반환
    }

    @Transactional
    public EditedResponseDto updateComment(Long postId, Long commentId, String wishComment, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("해당하는 유저가 존재하지 않습니다."));
        Post post = postRepositoy.findById(postId).orElseThrow(()-> new IllegalArgumentException("해당하는 게시글이 존재하지 않습니다."));
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new IllegalArgumentException("해당하는 댓글이 존재하지 않습니다."));

        if(!(user.getId().equals(post.getUser().getId()) || comment.getUser().getId().equals(user.getId()))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");
        }

        comment.setCommentContent(wishComment);

        return new EditedResponseDto(comment.getCommentContent());

    }

}
