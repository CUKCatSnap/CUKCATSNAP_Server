package com.cuk.catsnap.global.result.errorcode;

import com.cuk.catsnap.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OwnershipErrorCode implements ResultCode {
    NOT_FOUND_OWNERSHIP(404, "EO000", "해당 게시물의 소유권을 찾을 수 없습니다."),
    ;
    private final int status;
    private final String code;
    private final String message;
}
