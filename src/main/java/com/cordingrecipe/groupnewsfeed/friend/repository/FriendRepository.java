package com.cordingrecipe.groupnewsfeed.friend.repository;

import com.cordingrecipe.groupnewsfeed.friend.entity.Friend;
import com.cordingrecipe.groupnewsfeed.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    boolean existsByFromUserAndToUser(User fromUser, User toUser);

    Optional<Friend> findByFromUserIdAndToUserIdAndStatus(
            Long fromUserId, Long toUserId, Friend.FriendRequestStatus status);

    List<Friend> findByFromUserIdAndStatus(Long fromUserId, Friend.FriendRequestStatus status);

    List<Friend> findByToUserIdAndStatus(Long userId, Friend.FriendRequestStatus friendRequestStatus);

    Optional<Friend> findByFromUserIdAndToUserId(Long toUserId, Long fromUserId);


}