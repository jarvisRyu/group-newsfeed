package com.cordingrecipe.groupnewsfeed.friend.entity;

import com.cordingrecipe.groupnewsfeed.common.entity.BaseEntity;
import com.cordingrecipe.groupnewsfeed.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "friends")
public class Friends extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User fromUser;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User toUser;

    public enum FriendRequestStatus {
        PENDING,
        ACCEPTED,
        REJECTED
    }

}
