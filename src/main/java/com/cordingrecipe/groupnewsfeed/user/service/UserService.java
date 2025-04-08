package com.cordingrecipe.groupnewsfeed.user.service;

import com.cordingrecipe.groupnewsfeed.user.dto.UserLoginRequestDto;
import com.cordingrecipe.groupnewsfeed.user.dto.UserLoginResponseDto;
import com.cordingrecipe.groupnewsfeed.user.entity.User;
import com.cordingrecipe.groupnewsfeed.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserLoginResponseDto login(@Valid UserLoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(
                ()->new IllegalArgumentException ("아이디와 비밀번호를 정확히 입력해 주세요.")
        );
        if(!user.getPassword().equals(dto.getPassword())){
        throw new IllegalArgumentException ("아이디와 비밀번호를 정확히 입력해 주세요");
        }
        return new UserLoginResponseDto(
                user.getId(),
                user.getUserName());
    }
}
