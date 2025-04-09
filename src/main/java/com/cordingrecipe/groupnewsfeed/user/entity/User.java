package com.cordingrecipe.groupnewsfeed.user.entity;

import com.cordingrecipe.groupnewsfeed.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name="user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    @Column(unique = true)
    private String email;
    private String password;

    private boolean isDeleted = false;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void updateUser(String username, String email, String password){
        if (username != null) this.username = username;
        if (email != null) this.email = email;
        if (password != null) this.password = password;
    }



}
