package com.cordingrecipe.groupnewsfeed.post.entity;

import com.cordingrecipe.groupnewsfeed.comment.entity.Comment;
import com.cordingrecipe.groupnewsfeed.common.entity.BaseEntity;
import com.cordingrecipe.groupnewsfeed.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "Post")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    private void setUser(User user) {
        this.user = user;
    }

    public Post(User user, String contents) {
        this.user = user;
        this.contents = contents;
    }

    public static Post create(User user, String contents) {
        return new Post(user, contents);
    }

    public void updatePost(String contents) {
        if (contents != null) this.contents = contents;
    }

}
