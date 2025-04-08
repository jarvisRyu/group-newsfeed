package com.cordingrecipe.groupnewsfeed.user.service;

import com.cordingrecipe.groupnewsfeed.user.dto.UserLoginRequestDto;
import com.cordingrecipe.groupnewsfeed.user.dto.UserLoginResponseDto;
import com.cordingrecipe.groupnewsfeed.user.entity.User;
import com.cordingrecipe.groupnewsfeed.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserLoginResponseDto login(@Valid UserLoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(
                ()->new IllegalArgumentException ("해당 이메일이 존재하지 않습니다.")
        );
        if(passwordEncoder.matches(dto.getPassword(),user.getPassword())){
        throw new IllegalArgumentException ("비밀번호가 일치하지 않습니다.");
        }
        return new UserLoginResponseDto(
                user.getId(),
                user.getUserName());
    }
}
