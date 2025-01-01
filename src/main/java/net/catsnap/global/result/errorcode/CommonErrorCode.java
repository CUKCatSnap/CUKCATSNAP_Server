package net.catsnap.global.result.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.catsnap.global.result.ResultCode;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ResultCode {
    NOT_FOUND_API(404, "EC000", "존재하지 않는 API 입니다."),
    MISSING_REQUEST_PARAMETER(400, "EC001", "필수 쿼리 파라미터가 누락되었습니다."),
    INVALID_REQUEST_BODY(400, "EC002", "요청 바디가 잘못되었습니다."),
    INTERNAL_SERVER_ERROR(500, "EC003", "서버 내부 오류가 발생했습니다.");

    private final int status;
    private final String code;
    private final String message;
}
