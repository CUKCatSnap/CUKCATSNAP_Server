package com.cuk.catsnap.global.result.code;

import com.cuk.catsnap.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PhotographerResultCode implements ResultCode {
    PHOTOGRAPHER_SIGN_UP(201, "SP000", "사진작가 회원가입 성공"),
    LOOK_UP_MY_SETTING(200, "SP001", "사진작가 자신의 환경설정 조회 성공"),
    UPDATE_MY_SETTING(201, "SP002", "사진작가 자신의 환경설정 변경 성공"),
    LOOK_UP_RESERVATION_NOTICE(200, "SP003", "사진작가 자신의 에약 전 공지사항 조회 완료"),
    UPDATE_RESERVATION_NOTICE(201, "SP004", "사진작가 자신의 에약 전 공지사항 변경 완료"),
    LOOK_UP_RESERVATION_LOCATION(200, "SP005", "사진작가 자신이 에약 예약 받을 장소 조회 완료"),
    UPDATE_RESERVATION_LOCATION(201, "SP006", "사진작가 자신이 에약 예약 받을 장소를 변경 완료"),
    ;
    private final int status;
    private final String code;
    private final String message;
}
