package net.catsnap.global.result.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.catsnap.global.result.ResultCode;

@Getter
@RequiredArgsConstructor
public enum OwnershipErrorCode implements ResultCode {
    NOT_FOUND_OWNERSHIP(404, "EO000", "해당 게시물의 소유권을 찾을 수 없습니다."),
    NOT_FOUND_RESOURCE(404, "EO001", "해당 리소스를 찾을 수 없습니다."),
    ;
    private final int status;
    private final String code;
    private final String message;
}
