package com.cordingrecipe.groupnewsfeed.repository;


import com.cordingrecipe.groupnewsfeed.Exception.CustomException;
import com.cordingrecipe.groupnewsfeed.Exception.ErrorCode;
import com.cordingrecipe.groupnewsfeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserRepository extends JpaRepository<User, Long> {


    default User findByIdOrElseThrow(Long id){
        return findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

}
