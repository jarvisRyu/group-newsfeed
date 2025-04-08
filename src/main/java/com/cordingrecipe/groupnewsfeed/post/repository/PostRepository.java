package com.cordingrecipe.groupnewsfeed.post.repository;

import com.cordingrecipe.groupnewsfeed.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 예외 처리
    default Post findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id));
    }

}
