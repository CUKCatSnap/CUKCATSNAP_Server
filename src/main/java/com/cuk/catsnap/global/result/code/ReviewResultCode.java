package com.cuk.catsnap.global.result.code;

import com.cuk.catsnap.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReviewResultCode implements ResultCode {
    POST_REVIEW(201, "SV000", "새로운 리뷰를 업로드 했습니다."),
    REVIEW_LIEK_TOGGLE(200, "SV001", "리뷰 좋아요를 토글했습니다."),
    GET_REVIEW(200, "SV002", "성공적으로 리뷰를 조회하였습니다."),;
    private final int status;
    private final String code;
    private final String message;
}
