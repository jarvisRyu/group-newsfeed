package com.cordingrecipe.groupnewsfeed.service;


import com.cordingrecipe.groupnewsfeed.dto.*;
import com.cordingrecipe.groupnewsfeed.entity.User;


import java.util.List;


public interface UserService {
    // 유저 생성 기능
    SignUpResponseDto signUp(SignUpRequestDto requestDto);
    // 유저 단일 조회
    UserResponseDto findUser(Long id);
    // 유저 전체 조회
    List<UserResponseDto> findAll();
    // 유저 수정 기능
    User updateUser(Long id, UpdateUserRequestDto requestDto);
    // 유저 탍퇴 기능
    void signOut(Long id, String password);

}

