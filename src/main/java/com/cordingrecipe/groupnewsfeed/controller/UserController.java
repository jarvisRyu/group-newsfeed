package com.cordingrecipe.groupnewsfeed.controller;

import com.cordingrecipe.groupnewsfeed.dto.*;

import com.cordingrecipe.groupnewsfeed.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cordingrecipe.groupnewsfeed.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 1. 회원가입 기능
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signUp(@Valid @RequestBody SignUpRequsetDto requestDto){
        SignUpResponseDto signUpResponseDto = userService.signUp(requestDto);
        return new ResponseEntity<>(signUpResponseDto, HttpStatus.CREATED);

    }
    // 2. 유저 조회 기능
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findUser(@PathVariable Long id){
        UserResponseDto userResponseDto = userService.findUser(id);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }
    // 3. 전체 유저 조회 기능
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAll(){
        List<UserResponseDto> userList = userService.findAll();
        return new ResponseEntity<>(userList,HttpStatus.OK);
    }
    // 4. 유저 정보 수정 기능
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            HttpServletRequest httpServletRequest,
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequestDto requestDto
    ){
        LoginResponseDto longinUser = (LoginResponseDto) httpServletRequest.getSession().getAttribute(Const.LOGIN_USER);
        if(!longinUser.getId().equals(id)){
            throw new CustomException(ErrorCode.MISMATCH_USER);
        }
        User updateUser = userService.updateUser(id, requestDto);
        UserResponseDto userResponseDto = UserResponseDto.toDto(updatedUser);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }
    // 5. 유저 탈퇴 기능
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id, @RequestBody SignOutRequestDto requestDto){
        userService.delete(id,requestDto.getPassword());
        return new ResponseEntity<>("삭제가 완료되었습니다.", HttpStatus.OK);
    }

}
