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

    @Transactional
    public CreatePostResponseDto savePost(String contents, Long userId) { // Controller에서 받아온 작성된 게시글과 로그인유저ID

        // 로그인유저ID와 같은 유저ID를 usserRepositoty에서 찾기, 없으면 레포지토리에서 예외 처리
        User findUser = userRepository.findByIdOrElseThrow(userId);

        // 로그인유저ID와 유저ID가 같으면 로그인유저ID와 작성된 게시글 가져오기
        Post post = Post.create(findUser, contents);

        // 로그인유저ID와 작성된 게시글을 postRepository에 savePost 이름으로 저장
        Post savePost = postRepository.save(post);

        // 저장된 정보들인 로그인유저ID와 작성된 게시글 정보를 CreatePostResponseDto의 from 메서드의 매개변수로 넘겨준다.
        return CreatePostResponseDto.toDto(savePost);
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

        return CreatePostResponseDto.toDto(findPost);
    }

    @Transactional
    public UpdatePostResponseDto updatePost(Long id, String contents, Long userId) {

        // 게시글 조회
        Post findPost = postRepository.findByIdOrElseThrow(id);

        // 작성자(ID)와 현재 사용자(ID) 비교
        if (!findPost.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.POST_ACCESS_DENIED);
        }

        // 수정 로직
        findPost.updatePost(contents);

        return UpdatePostResponseDto.from(findPost);
    }

    @Transactional
    public void deletePostById(Long id, Long userId) {

        Post findPost = postRepository.findByIdOrElseThrow(id);

        // 작성자(ID)와 현재 사용자(ID) 비교
        if (!findPost.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.POST_ACCESS_DENIED);
        }

        // 삭제 로직
        postRepository.delete(findPost);
    }
}
