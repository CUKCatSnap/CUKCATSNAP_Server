package net.catsnap.global.result.errorcode;

import net.catsnap.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor

public enum PhotographerErrorCode implements ResultCode {
    DUPLICATED_SIGNUP_ID(409, "EP000", "중복된 ID로 회원가입이 불가능 합니다.");
    private final int status;
    private final String code;
    private final String message;
}
