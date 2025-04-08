package com.cordingrecipe.groupnewsfeed.service;


import com.cordingrecipe.groupnewsfeed.dto.SignUpRequsetDto;
import com.cordingrecipe.groupnewsfeed.dto.SignUpResponseDto;
import com.cordingrecipe.groupnewsfeed.dto.UpdateUserRequestDto;
import com.cordingrecipe.groupnewsfeed.dto.UserResponseDto;
import com.cordingrecipe.groupnewsfeed.entity.User;
import com.cordingrecipe.groupnewsfeed.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 회원가입 기능
    public SignUpResponseDto signUp(@Valid SignUpRequsetDto requestDto) {
        String hashedPassword = passwordEncoder.encode(requestDto.getPassword());
        User user = new User(requestDto.getUsername(), requestDto.getEmail(), hashedPassword);
        User savedUser = userRepository.save(user);

        return new SignUpResponseDto(savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail());
    }

    // 유저 단일 조회
    public UserResponseDto findUser(Long id) {
        User user = userRepository.findByIdOrElseThrow(id);

        return new UserResponseDto(user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }

    // 전체 유저 조회
    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream().map(UserResponseDto::toDto)
                .toList();
    }
    // 유저 수정 기능
    public User updateUser(Long id, UpdateUserRequestDto requestDto) {
        User savedUser = userRepository.findByIdOrElseThrow(id);

        String hashedPassword = passwordEncoder.encode(requestDto.getPassword());
        savedUser.updateUser(requestDto.getUsername(),requestDto.getEmail(),hashedPassword);

        return savedUser;
    }

    //회원삭제
    public void delete(Long id, String password) {

        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty()){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        User user = optionalUser.get();
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }

        userRepository.delete(user);
    }
}

