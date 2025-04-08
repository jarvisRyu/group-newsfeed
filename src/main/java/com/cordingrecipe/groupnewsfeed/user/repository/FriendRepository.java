package com.cordingrecipe.groupnewsfeed.user.repository;

import com.cordingrecipe.groupnewsfeed.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<User,Long> {
}
