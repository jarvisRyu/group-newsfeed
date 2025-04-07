package com.cordingrecipe.groupnewsfeed.user.controller;

import com.cordingrecipe.groupnewsfeed.user.dto.UserLoginRequestDto;
import com.cordingrecipe.groupnewsfeed.user.dto.UserLoginResponseDto;
import com.cordingrecipe.groupnewsfeed.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class LoginController {
    private final UserService userService;

    @PostMapping("/login") //user 로그인기능
    public ResponseEntity<UserLoginResponseDto> login(@Valid
                                                      @RequestBody UserLoginRequestDto dto, //dto:email,password
                                                      HttpServletRequest request) { //session
        UserLoginResponseDto loginUserId = userService.login(dto);//이메일,비밀번호로 id찾기

        HttpSession session = request.getSession(true); //세선값 가져오기
        session.setAttribute("LOGIN_USER", loginUserId);//session 에 정보저장
        return new ResponseEntity<>(loginUserId, HttpStatus.OK);//
    }
}
