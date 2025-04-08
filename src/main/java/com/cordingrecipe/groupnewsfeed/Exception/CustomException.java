package com.cordingrecipe.groupnewsfeed.Exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorcode){
        this.errorCode = errorcode;
    }
    public CustomException(ErrorCode errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }
}
