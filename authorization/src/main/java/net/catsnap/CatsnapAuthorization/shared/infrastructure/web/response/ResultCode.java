package net.catsnap.CatsnapAuthorization.shared.infrastructure.web.response;

import org.springframework.http.HttpStatusCode;

/**
 * 결과 코드 인터페이스
 * <p>
 * 결과 코드 형식: 1. 성공 코드는 "S"로 시작 (예: SC000, SM001) 2. 에러 코드는 "E"로 시작 (예: EC000, EM001) 3. 2번째 문자는 도메인
 * 카테고리를 나타냄 - A : authorization 모듈 - 기타 도메인별로 정의
 */
public interface ResultCode {

    /**
     * HTTP 상태 코드를 반환합니다. ResponseEntity의 상태 코드로 사용됩니다.
     */
    HttpStatusCode getHttpStatus();

    /**
     * 비즈니스 상태 코드를 반환합니다. 응답 body에 포함되어 더 세밀한 상태를 나타냅니다.
     */
    String getCode();

    /**
     * 상태에 대한 설명 메시지를 반환합니다.
     */
    String getMessage();
}
