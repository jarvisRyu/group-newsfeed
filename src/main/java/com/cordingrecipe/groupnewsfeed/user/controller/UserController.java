package com.cordingrecipe.groupnewsfeed.user.controller;

import com.cordingrecipe.groupnewsfeed.user.dto.*;
import com.cordingrecipe.groupnewsfeed.user.entity.User;
import com.cordingrecipe.groupnewsfeed.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jdk.jshell.spi.ExecutionControl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // 1. 회원가입 기능
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signUp(@Valid @RequestBody SignUpRequestDto requestDto) {
        SignUpResponseDto signUpResponseDto = userService.signUp(requestDto);
        return new ResponseEntity<>(signUpResponseDto, HttpStatus.CREATED);

    }

    // 2. 유저 조회 기능
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> findUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        UserResponseDto userResponseDto = userService.findUser(userId);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    // 2. 유저 조회 기능
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findUser(@PathVariable Long id) {
        UserResponseDto userResponseDto = userService.findUser(id);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    // 3. 전체 유저 조회 기능
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAll() {
        List<UserResponseDto> userList = userService.findAll();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    // 4. 유저 정보 수정 기능
    @PatchMapping("/me")
    public ResponseEntity<UserResponseDto> updateUser(

            HttpSession session,
            @Valid @RequestBody UpdateUserRequestDto requestDto
    ) {
        Long userId = (Long) session.getAttribute("userId");

        User updateUser = userService.updateUser(userId, requestDto);
        UserResponseDto userResponseDto = UserResponseDto.toDto(updateUser);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    // 4. 유저 정보 수정 기능
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(

            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequestDto requestDto
    ) {

        User updateUser = userService.updateUser(id, requestDto);
        UserResponseDto userResponseDto = UserResponseDto.toDto(updateUser);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }


    // 5. 유저 탈퇴 기능
    @DeleteMapping("/me")
    public ResponseEntity<String> deleteUser(HttpSession session, @RequestBody SignOutRequestDto requestDto) {
        Long userId = (Long) session.getAttribute("userId");
        userService.signOut(userId, requestDto.getPassword());
        return new ResponseEntity<>("삭제가 완료되었습니다.", HttpStatus.OK);
    }

    // 5. 유저 탈퇴 기능
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id, @RequestBody SignOutRequestDto requestDto) {

        userService.signOut(id, requestDto.getPassword());
        return new ResponseEntity<>("삭제가 완료되었습니다.", HttpStatus.OK);
    }

}
