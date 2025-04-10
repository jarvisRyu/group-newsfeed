package com.cordingrecipe.groupnewsfeed.user.service;

import com.cordingrecipe.groupnewsfeed.config.PasswordEncoder;
import com.cordingrecipe.groupnewsfeed.user.dto.SignUpRequestDto;
import com.cordingrecipe.groupnewsfeed.user.dto.SignUpResponseDto;
import com.cordingrecipe.groupnewsfeed.user.dto.UpdateUserRequestDto;
import com.cordingrecipe.groupnewsfeed.user.dto.UserResponseDto;
import com.cordingrecipe.groupnewsfeed.user.dto.UserLoginRequestDto;
import com.cordingrecipe.groupnewsfeed.user.dto.UserLoginResponseDto;
import com.cordingrecipe.groupnewsfeed.user.entity.User;
import com.cordingrecipe.groupnewsfeed.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        if(!passwordEncoder.matches(dto.getPassword(),user.getPassword())){
        throw new IllegalArgumentException ("비밀번호가 일치하지 않습니다.");
        }
        return new UserLoginResponseDto(
                user.getId(),
                user.getUsername());
    }

    //회원가입
    @jakarta.transaction.Transactional
    public SignUpResponseDto signUp(SignUpRequestDto requestDto) {

        String hashedPassword = passwordEncoder.encode(requestDto.getPassword());
        User user = new User(requestDto.getUsername(), requestDto.getEmail(), hashedPassword);
        User savedUser = userRepository.save(user);

        return new SignUpResponseDto(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }

    // 유저 단일 조회
    public UserResponseDto findUser(Long id) {

        User user = userRepository.findByIdOrElseThrow(id);

        return new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());
    }

    //유저 전체 조회
    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream().map(UserResponseDto::toDto).toList();
    }

    //유저 정보 수정
    @jakarta.transaction.Transactional
    public User updateUser(Long id, UpdateUserRequestDto requestDto) {
        //해당 유저 데이터 존재 여부 확인&불러오기
        User savedUser = userRepository.findByIdOrElseThrow(id);

        String hashedPassword = passwordEncoder.encode(requestDto.getPassword());
        savedUser.updateUser(requestDto.getUsername(), requestDto.getEmail(), hashedPassword);

        return savedUser;
    }

    //회원탈퇴
    @jakarta.transaction.Transactional
    public void signOut(Long id, String password) {

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
//            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        User user = optionalUser.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
//            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }

        user.setDeleted(true);
        userRepository.delete(user);
    }

}
