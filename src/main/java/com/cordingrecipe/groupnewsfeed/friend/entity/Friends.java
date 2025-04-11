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
@Table(name = "friends")
public class Friends extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // requestId로 추후에 쓰일 것임.

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User fromUser;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User toUser;

    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    private FriendRequestStatus status;

    public Friends() {
        
    }
    public static Friends rejected(User fromUser, User toUser) {
        return new Friends(fromUser, toUser, FriendRequestStatus.REJECTED);
    }

    public static Friends pending(User fromUser, User toUser) {
        return new Friends(fromUser, toUser, FriendRequestStatus.PENDING);
    }

    public static Friends accepted(User fromUser, User toUser) {
        return new Friends(fromUser, toUser, FriendRequestStatus.ACCEPTED);
    }

    public void acceptIfNotAccepted() {
        if(this.status == FriendRequestStatus.ACCEPTED) {
            throw new CustomException(ErrorCode.FRIEND_ALREADY_ACCEPTED);
        }
        this.status = FriendRequestStatus.ACCEPTED;
    }


    public void rejectRequest() {

        this.status = FriendRequestStatus.REJECTED;
    }

    public enum FriendRequestStatus {
        PENDING,
        ACCEPTED,
        REJECTED
    }

    public Friends(User fromUser, User toUser, FriendRequestStatus status) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.status = status;
    }

}
