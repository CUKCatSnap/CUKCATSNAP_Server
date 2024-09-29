package com.cuk.catsnap.global.result.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialResultCode {
    ;
    private final int status;
    private final String code;
    private final String message;
}
