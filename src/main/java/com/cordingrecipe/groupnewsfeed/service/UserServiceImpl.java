package com.cordingrecipe.groupnewsfeed.service;

import com.cordingrecipe.groupnewsfeed.Exception.CustomException;
import com.cordingrecipe.groupnewsfeed.Exception.ErrorCode;
import com.cordingrecipe.groupnewsfeed.config.PasswordEncoder;
import com.cordingrecipe.groupnewsfeed.dto.*;
import com.cordingrecipe.groupnewsfeed.entity.User;
import com.cordingrecipe.groupnewsfeed.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //회원가입
    @Override
    @Transactional
    public SignUpResponseDto signUp(SignUpRequestDto requestDto) {

        String hashedPassword = passwordEncoder.encode(requestDto.getPassword());
        User user = new User(requestDto.getUsername(), requestDto.getEmail(), hashedPassword);
        User savedUser = userRepository.save(user);

        return new SignUpResponseDto(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }

    // 유저 단일 조회
    @Override
    public UserResponseDto findUser(Long id) {

        User user = userRepository.findByIdOrElseThrow(id);

        return new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());
    }

    //유저 전체 조회
    @Override
    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream().map(UserResponseDto::toDto).toList();
    }

    //유저 정보 수정
    @Override
    @Transactional
    public User updateUser(Long id, UpdateUserRequestDto requestDto) {
        //해당 유저 데이터 존재 여부 확인&불러오기
        User savedUser = userRepository.findByIdOrElseThrow(id);

        String hashedPassword = passwordEncoder.encode(requestDto.getPassword());
        savedUser.updateUser(requestDto.getUsername(), requestDto.getEmail(), hashedPassword);

        return savedUser;
    }

    //회원탈퇴
    @Override
    @Transactional
    public void signOut(Long id, String password) {

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        User user = optionalUser.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }

        userRepository.delete(user);
    }
}
