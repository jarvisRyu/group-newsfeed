package com.cordingrecipe.groupnewsfeed.post.service;

import com.cordingrecipe.groupnewsfeed.post.dto.CreatePostResponseDto;
import com.cordingrecipe.groupnewsfeed.post.dto.UpdatePostResponseDto;
import com.cordingrecipe.groupnewsfeed.post.entity.Post;
import com.cordingrecipe.groupnewsfeed.post.repository.PostRepository;
import com.cordingrecipe.groupnewsfeed.user.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public CreatePostResponseDto savePost(String title, String contents, User user) {

        Post post = new Post(title, contents, user);

        Post savePost = postRepository.save(post);

        return new CreatePostResponseDto(
                savePost.getId(),
                savePost.getTitle(),
                savePost.getContents(),
                savePost.getCreatedAt()
        );

    }

    public List<CreatePostResponseDto> findAllPost() {

        return postRepository.findAll()
                .stream()
                .map(CreatePostResponseDto::toDto)
                .toList();
    }

    public CreatePostResponseDto findById(Long id) {

        Post findPost = postRepository.findByIdOrElseThrow(id);

        return new CreatePostResponseDto(
                findPost.getId(),
                findPost.getTitle(),
                findPost.getContents(),
                findPost.getCreatedAt()
        );
    }

    @Transactional
    public UpdatePostResponseDto updatePost(Long id, String title, String contents, HttpSession session) throws AccessDeniedException {

        // 세션에서 로그인된 사용자 ID 꺼내기
        Long sessionUserId = (Long) session.getAttribute("userId");
        if (sessionUserId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        // 게시글 조회
        Post findPost = postRepository.findByIdOrElseThrow(id);

        // 작성자와 현재 사용자 ID 비교
        if (!findPost.getUser().getId().equals(sessionUserId)) {
            throw new AccessDeniedException("작성자만 수정할 수 있습니다.");
        }

        // 수정 로직
        findPost.updatePost(title, contents);

        return new UpdatePostResponseDto(
                findPost.getId(),
                findPost.getTitle(),
                findPost.getContents(),
                findPost.getUpdatedAt()
        );
    }

    public void deletePostById(Long id, HttpSession session) throws AccessDeniedException {

        // 세션에서 로그인된 사용자 ID 꺼내기
        Long sessionUserId = (Long) session.getAttribute("userId");
        if (sessionUserId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        Post findPost = postRepository.findByIdOrElseThrow(id);

        // 작성자와 현재 사용자 ID 비교
        if (!findPost.getUser().getId().equals(sessionUserId)) {
            throw new AccessDeniedException("작성자만 삭제할 수 있습니다.");
        }

        // 삭제 로직
        postRepository.delete(findPost);
    }
}
