package com.cordingrecipe.groupnewsfeed.user.repository;

import com.cordingrecipe.groupnewsfeed.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    default User findByIdOrElseThrow(Long id){
        return findById(id)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND)"));
    }


    boolean existsByEmail(@Email(message = "이메일 형식으로 작성하십시오.") @NotBlank String email);
}
