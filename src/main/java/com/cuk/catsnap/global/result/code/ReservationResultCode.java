package com.cuk.catsnap.global.result.code;

import com.cuk.catsnap.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationResultCode implements ResultCode {
    RESERVATION_LOOK_UP(200, "SR200", "성공적으로 예약목록을 조회했습니다.");
    private final int status;
    private final String code;
    private final String message;
}
