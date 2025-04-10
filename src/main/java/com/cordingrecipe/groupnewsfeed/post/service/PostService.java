package com.cordingrecipe.groupnewsfeed.post.service;

import com.cordingrecipe.groupnewsfeed.common.advice.CustomException;
import com.cordingrecipe.groupnewsfeed.common.advice.ErrorCode;
import com.cordingrecipe.groupnewsfeed.post.dto.response.CreatePostResponseDto;
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

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CreatePostResponseDto savePost(String title, String contents, Long userId) {

        // 세션에서 로그인 유무 확인
        if (userId == null) {
            throw new CustomException(ErrorCode.USER_UNAUTHORIZED);
        }

        User findUser = userRepository.findByIdOrElseThrow(userId);

        Post post = new Post(findUser, title, contents);

        if (contents == null) {
            throw new CustomException(ErrorCode.POST_CONTENT_REQUIRED);
        }

        Post savePost = postRepository.save(post);

        return new CreatePostResponseDto(
                savePost.getId(),
                savePost.getUser().getUsername(),
                savePost.getTitle(),
                savePost.getContents(),
                savePost.getCreatedAt(),
                savePost.getUpdatedAt()
        );

    }

    // 페이징 기능으로 리펙토리
    public Page<CreatePostResponseDto> findAllPost(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));


        return postRepository.findAll(pageable)
                .map(CreatePostResponseDto::toDto);
    }

    @Transactional(readOnly = true)
    public CreatePostResponseDto findById(Long id) {

        Post findPost = postRepository.findByIdOrElseThrow(id);

        return new CreatePostResponseDto(
                findPost.getId(),
                findPost.getUser().getUsername(),
                findPost.getTitle(),
                findPost.getContents(),
                findPost.getCreatedAt(),
                findPost.getUpdatedAt()
        );
    }

    @Transactional
    public UpdatePostResponseDto updatePost(Long id, String title, String contents, Long userId) {

        // 세션에서 로그인 유무 확인
        if (userId == null) {
            throw new CustomException(ErrorCode.USER_UNAUTHORIZED);
        }

        // 게시글 조회
        Post findPost = postRepository.findByIdOrElseThrow(id);

        // 작성자(ID)와 현재 사용자(ID) 비교
        if (!findPost.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.POST_ACCESS_DENIED);
        }

        // 수정 로직
        findPost.updatePost(title, contents);
        if (contents == null) {
            throw new CustomException(ErrorCode.POST_CONTENT_REQUIRED);
        }

        return new UpdatePostResponseDto(
                findPost.getId(),
                findPost.getTitle(),
                findPost.getContents(),
                findPost.getUpdatedAt()
        );
    }

    @Transactional
    public void deletePostById(Long id, Long userId) {

        // 세션에서 로그인 유무 확인
        if (userId == null) {
            throw new CustomException(ErrorCode.USER_UNAUTHORIZED);
        }

        Post findPost = postRepository.findByIdOrElseThrow(id);

        // 작성자(ID)와 현재 사용자(ID) 비교
        if (!findPost.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.POST_ACCESS_DENIED);
        }

        // 삭제 로직
        postRepository.delete(findPost);
    }
}
