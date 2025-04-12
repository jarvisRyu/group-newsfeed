package com.cordingrecipe.groupnewsfeed.post.service;

import com.cordingrecipe.groupnewsfeed.comment.dto.CommentAllResponseDto;
import com.cordingrecipe.groupnewsfeed.comment.entity.Comment;
import com.cordingrecipe.groupnewsfeed.comment.repository.CommentRepository;
import com.cordingrecipe.groupnewsfeed.common.advice.CustomException;
import com.cordingrecipe.groupnewsfeed.common.advice.ErrorCode;
import com.cordingrecipe.groupnewsfeed.post.dto.response.CreatePostResponseDto;
import com.cordingrecipe.groupnewsfeed.post.dto.response.GetPostWhitCommentDto;
import com.cordingrecipe.groupnewsfeed.post.dto.response.UpdatePostResponseDto;
import com.cordingrecipe.groupnewsfeed.post.entity.Post;
import com.cordingrecipe.groupnewsfeed.post.repository.PostRepository;
import com.cordingrecipe.groupnewsfeed.user.entity.User;
import com.cordingrecipe.groupnewsfeed.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CreatePostResponseDto savePostBy(String contents, Long id) {

        // 로그인유저 조회
        User findUser = userRepository.findByIdOrElseThrow(id);

        // 게시글 조회
        Post post = Post.of(findUser, contents);

        // 게시글 저장
        Post savePost = postRepository.save(post);

        return CreatePostResponseDto.toDto(savePost);
    }

    // 페이징하여 전체 게시물 조회
    // @PageableDefault 활용으로 리팩토링
    public Page<CreatePostResponseDto> findAllPostBy(Pageable pageable) {

        return postRepository.findAll(pageable)
                .map(CreatePostResponseDto::toDto);
    }

    @Transactional
    public GetPostWhitCommentDto findBy(Long id) {

        // 게시글 조회
        Post findPost = postRepository.findByIdOrElseThrow(id);

        // 댓글 조회
        List<Comment> comments = commentRepository.findByPostIdOrderByUpdatedAtDesc(id);

        List<CommentAllResponseDto> commentList = comments.stream()
                .map(CommentAllResponseDto::toDto)
                .collect(Collectors.toList());

        return GetPostWhitCommentDto.from(findPost, commentList);
    }

    @Transactional
    public UpdatePostResponseDto updatePostBy(Long postid, String contents, Long userId) {

        // 게시글 조회
        Post findPost = postRepository.findByIdOrElseThrow(postid);

        // 작성자 검증
        findPost.vaildateWriter(userId);

        // 게시물 수정
        findPost.updatePost(contents);

        return UpdatePostResponseDto.from(findPost);
    }

    @Transactional
    public void deleteBy(Long id, Long userId) {

        // 게시글(ID) 조회
        Post findPost = postRepository.findByIdOrElseThrow(id);

        // 작성자 검증
        findPost.vaildateWriter(userId);

        // 삭제 로직
        postRepository.delete(findPost);
    }
}
