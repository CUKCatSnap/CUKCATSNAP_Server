package com.cuk.catsnap.global.result.code;

import com.cuk.catsnap.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FeedResultCode implements ResultCode {
    FEED_COMMENT_LOOK_UP(200, "SF000", "성공적으로 피드의 모든 댓글 조회를 완료하였습니다"),;
    private final int status;
    private final String code;
    private final String message;
}
