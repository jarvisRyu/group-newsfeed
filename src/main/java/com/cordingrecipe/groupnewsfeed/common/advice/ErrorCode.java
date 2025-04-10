package com.cordingrecipe.groupnewsfeed.common.advice;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND(404,"Not Found","해당 사용자를 찾을 수 없습니다."),
    USER_EMAIL_DUPLICATED(409,"Conflict","이미 사용 중인 이메일입니다."),
    USER_UNAUTHORIZED(401, "Unauthorized", "로그인이 필요합니다."),
    USER_PASSWORD_INVALID(400, "Bad Request", "비밀번호가 올바르지 않습니다."),
    USER_ALREADY_DELETED(410, "Gone", "이미 탈퇴한 사용자입니다."),
    WRONG_PASSWORD(401,"Wrong Password","비밀번호를 확인해 주세요."),

    POST_NOT_FOUND(404, "Not Found", "해당 게시글을 찾을 수 없습니다."),
    POST_TITLE_REQUIRED(400, "Bad Request", "게시글 제목은 필수입니다."),
    POST_CONTENT_REQUIRED(400, "Bad Request", "게시글 내용은 필수입니다."),
    POST_ACCESS_DENIED(403, "Forbidden", "해당 게시글에 대한 접근 권한이 없습니다."),

    COMMENT_CONTENT_REQUIRED(400, "Bad Request", "댓글 내용은 필수입니다."),
    COMMENT_ACCESS_DENIED(403, "Forbidden", "해당 댓글에 대한 권한이 없습니다.");

    private final int status;
    private final String error;
    private final String message;

}
