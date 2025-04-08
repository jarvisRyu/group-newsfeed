package com.cordingrecipe.groupnewsfeed.user.service;


import com.cordingrecipe.groupnewsfeed.friends.dto.CreateFriendRequestDto;
import com.cordingrecipe.groupnewsfeed.friends.dto.CreateFriendResponseDto;
import com.cordingrecipe.groupnewsfeed.user.repository.FriendRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;

    //친구 요청
    public CreateFriendResponseDto createFriend(User user, CreateFriendRequestDto createFriendRequestDto) {
        //로그인 유저 정보 조회 세션or시큐리티
        //receiverId로 수신자 존재여부 확인
        //친구 관계, 요청한 이력있는지 중복검사
        //친구 요청 객체 생성 PENDIG상태
        //db저장
        //응답 dto


    }

    //친구 요청 수락

    //친구 요청 거절

    //친구 목록 조회

    //친구 삭제
}
