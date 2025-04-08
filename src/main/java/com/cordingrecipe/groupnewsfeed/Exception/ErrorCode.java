package com.cordingrecipe.groupnewsfeed.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //common
    INVALID_INPUT_VALUE(400,"Bad Request", "C001","입력한 값을 확인해 주세요."),


    //user
    USER_LOGIN_FAIL(401, "Login Failed","U001","아이디와 비밀번호를 다시 확인해 주세요."),
    USER_NOT_FOUND(404, "Not Found", "U002", "존재하지 않는 사용자입니다"),
    WRONG_PASSWORD(401,"Wrong Password","U003", "비밀번호를 다시 확인해 주세요.");



    private final int status;
    private final String error;
    private final String code;
    private final String message;
}
