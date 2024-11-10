package com.cuk.catsnap.global.result.code;

import com.cuk.catsnap.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberResultCode implements ResultCode {
    MEMBER_SIGN_UP(201, "SM000", "회원가입 성공"),
    ;
    private final int status;
    private final String code;
    private final String message;
}
