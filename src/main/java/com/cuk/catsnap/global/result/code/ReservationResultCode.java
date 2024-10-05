package com.cuk.catsnap.global.result.code;

import com.cuk.catsnap.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationResultCode implements ResultCode {
    RESERVATION_LOOK_UP(200, "S000", "성공적으로 예약목록을 조회했습니다."),
    RESERVATION_AVAILABLE_TIME_LOOK_UP(200, "SR001", "성공적으로 예약 가능한 시간을 조회했습니다."),
    RESERVATION_PROGRAM_LOOK_UP(200, "SR002", "성공적으로 예약 가능한 프로그램을 조회했습니다."),
    RESERVATION_GUIDANCE_LOOK_UP(200, "SR003", "성공적으로 예약 주의사항과 예약 가능한 장소를 조회했습니다."),
    RESERVATION_BOOK_COMPLETE(201, "SR004", "예약이 성공적으로 완료되었습니다.");
    private final int status;
    private final String code;
    private final String message;
}
