package com.cordingrecipe.groupnewsfeed.user.service;

import com.cordingrecipe.groupnewsfeed.common.advice.CustomException;
import com.cordingrecipe.groupnewsfeed.common.advice.ErrorCode;
import com.cordingrecipe.groupnewsfeed.config.PasswordEncoder;
import com.cordingrecipe.groupnewsfeed.user.dto.*;
import com.cordingrecipe.groupnewsfeed.user.entity.User;
import com.cordingrecipe.groupnewsfeed.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserLoginResponseDto login(@Valid UserLoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(
                ()->new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        if(user.isDeleted()){
            throw new CustomException(ErrorCode.USER_ALREADY_DELETED);
        }
        if(!passwordEncoder.matches(dto.getPassword(),user.getPassword())){
        throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }
        return new UserLoginResponseDto(
                user.getId(),
                user.getUsername());
    }

    //회원가입
    @Transactional
    public SignUpResponseDto signUp(SignUpRequestDto requestDto) {

        //이메일 중복시 에러
        if(userRepository.existsByEmail(requestDto.getEmail())){
            throw new CustomException(ErrorCode.USER_EMAIL_DUPLICATED);
        }

        String hashedPassword = passwordEncoder.encode(requestDto.getPassword());
        User user = User.register(requestDto,hashedPassword);
        User savedUser = userRepository.save(user);

        return new SignUpResponseDto(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }

    // 유저 단일 조회
    @Transactional(readOnly = true)
    public FindUserIdResponseDto findUser(Long id) {

        User user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return FindUserIdResponseDto.toDto(user);
    }

    //유저 전체 조회
    @Transactional(readOnly = true)
    public List<UserResponseDto> findAll() {

        List<User> users = userRepository.findByIsDeletedFalse();

        return users.stream().map(UserResponseDto::toDto).collect(Collectors.toList());

    }

    //유저 정보 수정
    @Transactional
    public User updateUser(Long id, UpdateUserRequestDto requestDto) {
        //해당 유저 데이터 존재 여부 확인&불러오기
        User savedUser = userRepository.findByIdOrElseThrow(id);

        String hashedPassword = passwordEncoder.encode(requestDto.getPassword());
        savedUser.updateUser(requestDto.getUsername(), requestDto.getEmail(), hashedPassword);

        return savedUser;
    }

    //회원탈퇴
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

        user.softDeleted();

    }

    @Transactional
    public void updateIntroduction(Long id, UpdateIntroductionRequestDto dto) {
        User user = userRepository.findByIdOrElseThrow(id);
        user.updateIntroduction(dto.getIntroduction());
    }
}
