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
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class LoginController {
    private final UserService userService;

    @PostMapping("/login") //user 로그인기능
    public ResponseEntity<String>  login(@Valid
                                                      @RequestBody UserLoginRequestDto dto, //dto:email,password
                                                      HttpServletRequest request) { //session
        UserLoginResponseDto loginUserDto = userService.login(dto);//이메일,비밀번호로 id 찾기

        HttpSession session = request.getSession(true); //세선값 가져오기,없으면생성
        session.setAttribute("LOGIN_USER", loginUserDto);//session 에 정보저장
        String wellComeMessage="로그인성공\n"+loginUserDto.getUserName()+"님 반갑습니다.";
        return new ResponseEntity<>(wellComeMessage, HttpStatus.OK);
    }

    @PostMapping("/logout") //user 로그아웃
    public ResponseEntity<String> logout(HttpServletRequest request){
        HttpSession session = request.getSession(); //요청에 담긴 session 가져옴
        if(session !=null){
            session.invalidate();
        }
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

//    @PatchMapping("/{id}")
//    public ResponseEntity<>
}

