package com.cordingrecipe.groupnewsfeed.post.service;

import com.cordingrecipe.groupnewsfeed.post.dto.CreatPostRequestDto;
import com.cordingrecipe.groupnewsfeed.post.dto.CreatePostResponseDto;
import com.cordingrecipe.groupnewsfeed.post.dto.UpdatePostResponseDto;
import com.cordingrecipe.groupnewsfeed.post.entity.Post;
import com.cordingrecipe.groupnewsfeed.post.repository.PostRepository;
import com.cordingrecipe.groupnewsfeed.user.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class Postservice {

    private final PostRepository postRepository;

    public Postservice(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public CreatePostResponseDto savePost(String title, String contents) {

        Post post = new Post(title, contents);

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
    public UpdatePostResponseDto updatePost(Long id, String title, String contents) {

        Post findPost = postRepository.findByIdOrElseThrow(id);

        findPost.updatePost(title, contents);

        return new UpdatePostResponseDto(
                findPost.getId(),
                findPost.getTitle(),
                findPost.getContents(),
                findPost.getUpdatedAt()
        );
    }

    public void deletePostById(Long id) {

        Post findPost = postRepository.findByIdOrElseThrow(id);

        postRepository.delete(findPost);
    }
}
