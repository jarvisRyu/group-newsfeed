package com.cordingrecipe.groupnewsfeed.friend.entity;

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
