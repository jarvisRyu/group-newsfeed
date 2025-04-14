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
                ? fieldError.getDefaultMessage() : ErrorCode.VALIDATION_FAILED.getMessage();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(ErrorCode.VALIDATION_FAILED.getStatus())
                .error(ErrorCode.VALIDATION_FAILED.getError())
                .message(errorMessage)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    //NPE 예외처리
    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.NULL_POINTER_EXCEPTION);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.ILLEGAL_ARGUMENT);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e){
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.ACCESS_DENIED);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(ResponseStatusException.class)
    protected ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException e){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(ErrorCode.RESPONSE_STATUS_ERROR.getStatus())
                .error(ErrorCode.RESPONSE_STATUS_ERROR.getError())
                .message(e.getReason() != null ? e.getReason() : ErrorCode.RESPONSE_STATUS_ERROR.getMessage())
                .build();
        return ResponseEntity.status(ErrorCode.RESPONSE_STATUS_ERROR.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(ErrorCode.RUNTIME_EXCEPTION.getStatus())
                .error(ErrorCode.RUNTIME_EXCEPTION.getError())
                .message(e.getMessage() != null ? e.getMessage() : ErrorCode.RUNTIME_EXCEPTION.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }


    //모든 예외처리
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }


}
