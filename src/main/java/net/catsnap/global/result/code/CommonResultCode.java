package net.catsnap.global.result.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.catsnap.global.result.ResultCode;

@Getter
@RequiredArgsConstructor
public enum CommonResultCode implements ResultCode {
    COMMON_LOOK_UP(200, "SC000", "성공적으로 데이터를 조회했습니다."),
    COMMON_CREATE(201, "SC001", "성공적으로 데이터를 생성했습니다."),
    COMMON_REVISE(200, "SC002", "성공적으로 데이터를 수정했습니다."),
    ;

    private final int status;
    private final String code;
    private final String message;
}
