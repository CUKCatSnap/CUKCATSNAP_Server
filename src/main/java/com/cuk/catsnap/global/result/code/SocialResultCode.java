package com.cuk.catsnap.global.result.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialResultCode {
    SOCIAL_LIST_LOOK_UP(200, "SS000", "구독중(차단한) 작가, 장소 조회 완료");
    private final int status;
    private final String code;
    private final String message;
}
