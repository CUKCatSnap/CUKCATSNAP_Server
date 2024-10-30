package com.cuk.catsnap.global.result.code;

import com.cuk.catsnap.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PhotographerResultCode implements ResultCode {
    PHOTOGRAPHER_SIGN_UP(201, "SP000", "사진작가 회원가입 성공"),
    LOOK_UP_MY_SETTING(200, "SP001", "사진작가 자신의 환경설정 조회 성공"),;
    private final int status;
    private final String code;
    private final String message;
}
