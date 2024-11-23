package com.cuk.catsnap.global.result.errorcode;

import com.cuk.catsnap.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SecurityErrorCode implements ResultCode {
    WRONG_ID_OR_PASSWORD(401, "EY000", "아이디 또는 비밀번호가 일치하지 않습니다."),
    BAD_API_FORM(400, "EY001", "잘못된 로그인 API 요청 형식입니다."),
    NOT_AUTHENTICATED(401, "EY002", "인증되지 않은 사용자입니다."),
    WRONG_JWT_TOKEN(400, "EY003", "JWT 토큰 서명이 올바르지 않습니다."),
    EXPIRED_JWT_TOKEN(401, "EY004", "JWT 토큰이 만료되었습니다."),
    EXPIRED_REFRESH_TOKEN(401, "EY005", "리프레시 토큰이 만료되었습니다."),
    ;

    private final int status;
    private final String code;
    private final String message;
}
