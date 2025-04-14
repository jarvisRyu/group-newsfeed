package com.cordingrecipe.groupnewsfeed.user.controller;

import com.cordingrecipe.groupnewsfeed.common.constant.Const;
import com.cordingrecipe.groupnewsfeed.user.dto.UserLoginRequestDto;
import com.cordingrecipe.groupnewsfeed.user.dto.UserLoginResponseDto;
import com.cordingrecipe.groupnewsfeed.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class LoginController {
    private final UserService userService;

    @PostMapping("/login") //user 로그인기능
    public ResponseEntity<String>  login(@Valid @RequestBody UserLoginRequestDto dto,
                                         HttpSession session) {
        UserLoginResponseDto loginUserDto = userService.login(dto);
        session.setAttribute(Const.LOGIN_USER, loginUserDto);
        String wellComeMessage="로그인성공\n"+loginUserDto.getUserName()+"님 반갑습니다.";
        return new ResponseEntity<>(wellComeMessage, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session ){
        if(session !=null){
            session.invalidate();
        }
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }
}

