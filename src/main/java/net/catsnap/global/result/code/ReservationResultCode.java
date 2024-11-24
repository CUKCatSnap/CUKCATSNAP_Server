package net.catsnap.global.result.code;

import net.catsnap.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationResultCode implements ResultCode {
    RESERVATION_LOOK_UP(200, "S000", "성공적으로 예약목록을 조회했습니다."),
    RESERVATION_AVAILABLE_TIME_LOOK_UP(200, "SR001", "성공적으로 예약 가능한 시간을 조회했습니다."),
    RESERVATION_PROGRAM_LOOK_UP(200, "SR002", "성공적으로 예약 가능한 프로그램을 조회했습니다."),
    RESERVATION_GUIDANCE_LOOK_UP(200, "SR003", "성공적으로 예약 주의사항과 예약 가능한 장소를 조회했습니다."),
    RESERVATION_BOOK_COMPLETE(201, "SR004", "예약이 성공적으로 완료되었습니다."),
    PHOTOGRAPHER_RESERVATION_STATE_CHANGE(201, "SR005", "성공적으로 예약 상태를 변경했습니다."),
    PHOTOGRAPHER_RESERVATION_TIME_FORMAT(201, "SR006", "성공적으로 예약 시간 형식을 추가(수정)하였습니다."),
    PHOTOGRAPHER_RESERVATION_TIME_FORMAT_DELETE(20, "SR007", "성공적으로 예약 시간 형식을 삭제했습니다."),
    PHOTOGRAPHER_RESERVATION_TIME_FORMAT_MAPPING_WEEKDAY(201, "SR008",
        "성공적으로 예약 시간 형식을 특정 요일에 등록했습니다."),
    PHOTOGRAPHER_RESERVATION_TIME_FORMAT_UNMAPPING_WEEKDAY(200, "SR009",
        "성공적으로 예약 시간 형식을 특정 요일에 해제했습니다."),
    PHOTOGRAPHER_POST_PROGRAM(201, "SR010", "성공적으로 예약 프로그램을 등록(수정)했습니다."),
    PHOTOGRAPHER_DELETE_PROGRAM(200, "SR011", "성공적으로 예약 프로그램을 삭제했습니다."),
    PHOTOGRAPHER_RESERVATION_TIME_FORMAT_LOOK_UP(200, "SR012", "성공적으로 예약 시간 형식을 조회했습니다."),
    PHOTOGRAPHER_LOOK_UP_PROGRAM(200, "SR013", "성공적으로 예약 프로그램을 조회회했습니다."),
    ;

    private final int status;
    private final String code;
    private final String message;
}
