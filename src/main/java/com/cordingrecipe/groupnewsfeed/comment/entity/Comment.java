package com.cordingrecipe.groupnewsfeed.comment.entity;

import com.cordingrecipe.groupnewsfeed.common.entity.BaseEntity;
import com.cordingrecipe.groupnewsfeed.post.entity.Post;
import com.cordingrecipe.groupnewsfeed.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "comment")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private String commentContent;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    // 댓글 생성 정적 메서드
    public static Comment create(User user, Post post, String commentContent) {
        return new Comment(user, post, commentContent);
    }

    public Comment(User user, Post post, String commentContent){
      this.user = user;
      this.post = post;
      this.commentContent = commentContent;
    }

    // 댓글 업데이트 메서드
    public void updateComment(String wishComment){
        if (wishComment != null){
            this.commentContent = wishComment;
        }
    }

    // 로그인된 유저와 댓글 작성자가 같은지 확인하는 메서드
    public boolean hasDeleteRole(Long commentId, Long userId){
        if(!(commentId.equals(userId))){
            return false;
        } else{
            return true;
        }
    }

}