package com.cordingrecipe.groupnewsfeed.common.advice;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)//null 값은 Json에 포함시키지 않음.
public class ErrorResponse {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status; //상태코드
    private final String error; //상태코드 메세지
    private final String message;

    public ErrorResponse(ErrorCode errorCode) {
        this.status = errorCode.getStatus();
        this.error = errorCode.getError();
        this.message = errorCode.getMessage();
    }

}

