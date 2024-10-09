package com.cuk.catsnap.global.result;

/**
 * 결과 코드에 대한 Enum class
 * 결과 코드 형식
 * 1. 모든 결과 코드는 "S"로 시작한다.
 * 1-1. Error가 발생했을 때, 결과 코드는 "E"로 시작한다.
 * 2. 2번째 문자는 결과가 발생한 카테고리를 나타낸다.
 * ex) "M": Member, "F": Feed, "R": Reservation
 *
 */

public interface ResultCode {

    int getStatus();
    String getCode();
    String getMessage();
}
