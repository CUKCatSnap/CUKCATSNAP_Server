package com.cuk.catsnap.global.result.code;

import com.cuk.catsnap.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SearchResultCode implements ResultCode {
    Feed_Search(200,"SS000", "성공적으로 피드 검색을 완료하였습니다"),
    Review_Search(200,"SS001", "성공적으로 리뷰 검색을 완료하였습니다");
    private final int status;
    private final String code;
    private final String message;
}
