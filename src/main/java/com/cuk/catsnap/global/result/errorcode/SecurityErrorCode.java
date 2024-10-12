package com.cuk.catsnap.global.result.errorcode;

import com.cuk.catsnap.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SecurityErrorCode implements ResultCode {
    WRONG_ID_OR_PASSWORD(401,"EY000", "아이디 또는 비밀번호가 일치하지 않습니다."),
    BAD_API_FORM(400,"EY001", "잘못된 로그인 API 요청 형식입니다.");

    private final int status;
    private final String code;
    private final String message;
}
