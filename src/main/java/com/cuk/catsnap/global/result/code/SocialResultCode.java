package com.cuk.catsnap.global.result.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialResultCode {
    SOCIAL_LIST_LOOK_UP(200, "So000", "구독중(차단한) 작가, 장소 조회 완료"),
    SUBSCRIBE_PHOTOGRAPHER_TOGGLE(200, "So001", "작가 구독(구독 취소)를 토글 했습니다."),
    BLOCK_PHOTOGRAPHER_TOGGLE(200, "So002", "작가 차단(차단 취소)를 토글 했습니다."),
    SUBSCRIBE_PLACE(201, "So003", "해당 장소를 구독하였습니다."),
    SUBSCRIBE_CANCEL_PLACE(200, "So004", "해당 장소 구독을 취소 하였습니다."),;
    private final int status;
    private final String code;
    private final String message;
}
