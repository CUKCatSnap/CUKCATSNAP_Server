package net.catsnap.global.result.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.catsnap.global.result.ResultCode;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ResultCode {
    NOT_FOUND_API(404, "EC000", "존재하지 않는 API 입니다."),
    ;

    private final int status;
    private final String code;
    private final String message;
}
