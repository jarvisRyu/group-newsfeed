package com.cordingrecipe.groupnewsfeed.comment.repository;

import com.cordingrecipe.groupnewsfeed.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
