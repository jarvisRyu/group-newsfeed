package com.cordingrecipe.groupnewsfeed.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;



@Slf4j
public class LoginFilter implements Filter {

    private static final String[] WHITE_LIST = {"/", "/api/users/signup", "/api/users/login"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;//다운캐스팅
        HttpServletResponse httpResponse = (HttpServletResponse) response;//다운캐스팅
        String requestURI = httpRequest.getRequestURI();//사용자 요청 URI 가져오기

        if (!isWhiteList(requestURI)) { //리스트 조회
            HttpSession session = httpRequest.getSession(false);//session 이 존재하는지 확인 , 생성X
            if (session == null || session.getAttribute("LOGIN_USER") == null) {//세션이없거나 LOGIN_USER 에 정보가 없으면
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인이 필요합니다.");   //401 오류발생
                return;
            }
        }
        chain.doFilter(request, response); //인증성공시 전달
    }
    private boolean isWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI); //요청에 입력된 URI 가 WHTIE LIST 에 포함되있는지 확인
    }
}


