package com.cuk.catsnap.global.result.errorcode;

import com.cuk.catsnap.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationErrorCode implements ResultCode {
    CANNOT_CHANGE_RESERVATION_STATE(400, "ER000", "해당 예약 상태에서 요청하신 예약상태로 변경할 수 없습니다."),
    CAN_NOT_FOUND_PROGRAM(404, "EP001", "존재하지 않는 프로그램입니다."),
    DELETED_PROGRAM(404, "EP002", "삭제된 프로그램입니다."),;
    private final int status;
    private final String code;
    private final String message;
}
