package com.cordingrecipe.groupnewsfeed.repository;


import com.cordingrecipe.groupnewsfeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {


    default User findByIdOrElseThrow(Long id){
        return findById(id)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND)"));
    }
    Optional<User> findByIdAndIsDeletedFalse(Long id);

}
