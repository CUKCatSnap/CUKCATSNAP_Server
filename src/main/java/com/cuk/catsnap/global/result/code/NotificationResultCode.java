package com.cuk.catsnap.global.result.code;

import com.cuk.catsnap.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationResultCode implements ResultCode {
    NOTIFICATION_LOOK_UP(200, "SN000", "성공적으로 알림 조회를 완료하였습니다"),
    NOTIFICATION_READ(200, "SN001", "해당 알림을 성공적으로 읽음 차리하였습니다"),
    NOTIFICATION_DELETE(200, "SN002", "성공적으로 알림 삭제를 완료하였습니다"),
    ;
    private final int status;
    private final String code;
    private final String message;
}
