package net.catsnap.global.result.code;

import net.catsnap.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SecurityResultCode implements ResultCode {
    COMPLETE_SIGN_IN(200, "SY000", "로그인을 성공적으로 수행"),
    COMPLETE_REFRESH_TOKEN(200, "SY001", "토큰 재발급을 성공적으로 수행"),
    ;

    private final int status;
    private final String code;
    private final String message;
}
