package net.catsnap.CatsnapReservation.shared;

import org.springframework.http.HttpStatusCode;

/**
 * 결과 코드 인터페이스
 * <p>
 * 모든 레이어에서 공통으로 사용하는 결과 코드 인터페이스입니다.
 * 도메인 에러와 프레젠테이션 에러 모두 이 인터페이스를 구현합니다.
 * </p>
 * <p>
 * 결과 코드 형식:
 * <ul>
 *   <li>성공 코드는 "S"로 시작 (예: SC000, SM001)</li>
 *   <li>에러 코드는 "E"로 시작 (예: EC000, EM001)</li>
 *   <li>2번째 문자는 도메인 카테고리를 나타냄 - R : reservation 모듈</li>
 * </ul>
 * </p>
 */
public interface ResultCode {

    /**
     * HTTP 상태 코드를 반환합니다.
     * <p>
     * ResponseEntity의 상태 코드로 사용됩니다.
     * </p>
     */
    HttpStatusCode getHttpStatus();

    /**
     * 비즈니스 상태 코드를 반환합니다.
     * <p>
     * 응답 body에 포함되어 더 세밀한 상태를 나타냅니다.
     * </p>
     */
    String getCode();

    /**
     * 상태에 대한 설명 메시지를 반환합니다.
     */
    String getMessage();
}
