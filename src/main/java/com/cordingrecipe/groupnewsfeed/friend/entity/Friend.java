package com.cordingrecipe.groupnewsfeed.friend.entity;

import com.cordingrecipe.groupnewsfeed.common.advice.CustomException;
import com.cordingrecipe.groupnewsfeed.common.advice.ErrorCode;
import com.cordingrecipe.groupnewsfeed.common.entity.BaseEntity;
import com.cordingrecipe.groupnewsfeed.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Entity
@Table(name = "friend")
public class Friend extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    @ManyToOne
    @JoinColumn(name = "to_user_id")
    private User toUser;

    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    private FriendRequestStatus status;

    public Friend() {}

    public void pending() {
        this.status = FriendRequestStatus.PENDING;
    }

    public void acceptIfPending() {
        if(this.status == FriendRequestStatus.ACCEPTED) {
            throw new CustomException(ErrorCode.FRIEND_ALREADY_ACCEPTED);
        }
        this.status = FriendRequestStatus.ACCEPTED;
    }

    public void rejectRequest() {this.status = FriendRequestStatus.REJECTED;
    }

    public void accepted() {
        this.status = FriendRequestStatus.ACCEPTED;
    }

    public enum FriendRequestStatus {
        PENDING,
        ACCEPTED,
        REJECTED
    }

    public Friend(User fromUser, User toUser) {
        this.fromUser = fromUser;
        this.toUser = toUser;
    }

    public void ensureReceiverIs(Long userId) {
        if (!this.toUser.getId().equals(userId)) {
            throw new CustomException(ErrorCode.FRIEND_ACCESS_DENIED);
        }
    }
}
