package com.cordingrecipe.groupnewsfeed.friend.repository;
import com.cordingrecipe.groupnewsfeed.friend.entity.Friends;
import com.cordingrecipe.groupnewsfeed.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FriendRepository extends JpaRepository<Friends,Long> {
    boolean  existsByFromUserAndToUser(User fromUser, User toUser);
}
