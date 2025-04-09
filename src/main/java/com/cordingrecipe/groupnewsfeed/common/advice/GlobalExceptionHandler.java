package com.cordingrecipe.groupnewsfeed.common.advice;

import lombok.Builder;;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /* 양식
     * {
     * "timestamp":"2025-04-08 00:00:00",
     *  "status" : 400
     * "error": "Bad_Request",
     * "message":"XX 에러발생",
     */

    //Custom Exception 처리
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse errorResponse = new ErrorResponse(errorCode);
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
        //ResponseEntity.status=HTTP 응답 헤더 상태코드
        //.body = 응답바디(Json)
    }

    //Valid 예외처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();//메세지없으면 예외발생할수있음
        String errorMessage = (fieldError != null && fieldError.getDefaultMessage() != null)
                ? fieldError.getDefaultMessage() : "잘못된 요청입니다.";
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(e.getStatusCode().value())
                .error("Validation Failed")
                .message(errorMessage)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    //NPE 예외처리
    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(400)
                .error("Null Pointer Exception")
                .message(e.getMessage() != null ? e.getMessage() : "처리 중 null 값이 참조되었습니다.")
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(400)
                .error("Illegal Argument")
                .message(e.getMessage() != null ? e.getMessage() : "잘못된 요청입니다.")
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(403)
                .error("Access Denied")
                .message(e.getMessage() != null ? e.getMessage() : "접근 권한이 없습니다.")
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(ResponseStatusException.class)
    protected ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException e){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(e.getStatusCode().value())
                .error("Response Status Error")
                .message(e.getReason() != null ? e.getReason() : "에러가 발생했습니다.")
                .build();
        return ResponseEntity.status(e.getStatusCode()).body(errorResponse);
    }

    //모든 예외처리
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(500)
                .error("Internal Server Error")
                .message(e.getMessage() != null ? e.getMessage() : "예기치 못한 오류가 발생했습니다.")
                .build();
        return ResponseEntity.status(500).body(errorResponse);
    }


}
