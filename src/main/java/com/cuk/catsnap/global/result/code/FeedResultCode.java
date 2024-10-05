package com.cuk.catsnap.global.result.code;

import com.cuk.catsnap.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FeedResultCode implements ResultCode {
    FEED_COMMENT_LOOK_UP(200, "SF000", "성공적으로 피드의 모든 댓글 조회를 완료하였습니다"),
    POST_FEED_COMMENT(201, "SF001", "성공적으로 피드에 댓글을 작성하였습니다."),
    DELETE_FEED_COMMENT(200, "SF002", "성공적으로 피드의 댓글을 삭제하였습니다."),
    FEED_COMMENT_LIKE_TOGGLE(200, "SF003", "성공적으로 피드의 댓글 좋아요를 토글했습니다."),
    FEED_LIKE_TOGGLE(200, "SF004", "성공적으로 피드에 좋아요를 토글했습니다."),
    POST_FEED(201, "SF005", "성공적으로 피드를 작성하였습니다."),;
    private final int status;
    private final String code;
    private final String message;
}
