package com.cordingrecipe.groupnewsfeed.friend.repository;

import com.cordingrecipe.groupnewsfeed.friend.entity.Friends;
import com.cordingrecipe.groupnewsfeed.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friends, Long> {

    boolean existsByFromUserAndToUser(User fromUser, User toUser);

    Optional<Friends> findByFromUserIdAndToUserIdAndStatus(
            Long requesterId, Long userId, Friends.FriendRequestStatus status);

    List<Friends> findByFromUserIdAndStatus(
            Long fromUserId, Friends.FriendRequestStatus status);

    List<Friends> findByToUserIdAndStatus(Long userId, Friends.FriendRequestStatus friendRequestStatus);

    Optional<Friends> findByFromUserIdAndToUserId(Long id, Long id1);

    boolean existsByFromUserIdAndToUserIdAndStatus(Long fromUserId, Long toUserId, Friends.FriendRequestStatus status);
}