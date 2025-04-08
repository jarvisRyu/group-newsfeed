package com.cordingrecipe.groupnewsfeed.repository;


import com.cordingrecipe.groupnewsfeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    default User findByIdOrElseThrow(Long id){
        return findById(id)
                .orElseThrow(() -> new CustomException(Errorcode.USER_NOT_FOUND));
    }

}
