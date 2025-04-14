package com.cordingrecipe.groupnewsfeed.user.entity;

import com.cordingrecipe.groupnewsfeed.common.entity.BaseEntity;
import com.cordingrecipe.groupnewsfeed.post.entity.Post;
import com.cordingrecipe.groupnewsfeed.user.dto.SignUpRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@NoArgsConstructor
@Table(name = "user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String username;

    @Column(unique = true)
    private String email;

    @Column(nullable = true)
    private String password;

    @Column(length = 200)
    private String introduction = "";


    @Column(name = "is_deleted", nullable=false)
    private boolean isDeleted;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void updateUser(String username, String password) {
        if (username != null) this.username = username;

        if (password != null) this.password = password;
    }

    public void updateIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void softDeleted(){
        this.isDeleted = true;
    }


    // 정적 메서드 추가
    public static User register(SignUpRequestDto dto, String hashedPassword){
       User user = new User(
               dto.getUsername(),
               dto.getEmail(),
               hashedPassword
        );

        return user;
    }

    // 로그인된 유저와 게시물 작성자가 같은지 확인하는 메서드
    public boolean hasDeleteRole(Long userId, Post post){
        if(userId.equals(post.getUser().getId())){
            return true;
        } else{
            return false;
        }
    }

}
