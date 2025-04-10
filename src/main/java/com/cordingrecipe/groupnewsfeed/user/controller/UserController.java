package com.cordingrecipe.groupnewsfeed.user.controller;

import com.cordingrecipe.groupnewsfeed.common.filter.Const;
import com.cordingrecipe.groupnewsfeed.user.dto.*;
import com.cordingrecipe.groupnewsfeed.user.entity.User;
import com.cordingrecipe.groupnewsfeed.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/{id}")
    public ResponseEntity<FindUserIdResponseDto> findUser(@PathVariable Long id) {
        FindUserIdResponseDto findUserById  = userService.findUser(id);
        return new ResponseEntity<>(findUserById, HttpStatus.OK);
    }

    // 3. 전체 유저 조회 기능
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAll() {
        List<UserResponseDto> userList = userService.findAll();
        return new ResponseEntity<>(userList, HttpStatus.OK);
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
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id, @RequestBody SignOutRequestDto requestDto) {
        userService.signOut(id, requestDto.getPassword());
        return new ResponseEntity<>("삭제가 완료되었습니다.", HttpStatus.OK);
    }

    //6.유저 자기소개글
    @PatchMapping("/introduction")
    public ResponseEntity<String> updateIntroduction(@RequestBody UpdateIntroductionRequestDto dto,
                                                   HttpServletRequest request){
        HttpSession session = request.getSession();
        UserLoginResponseDto loginUser = (UserLoginResponseDto) session.getAttribute(Const.LOGIN_USER);
        Long loginUserId= loginUser.getId();

        userService.updateIntroduction(loginUserId,dto);
        return new ResponseEntity<>("자기소개글이 등록되었습니다.",HttpStatus.OK);
    }

}
