package net.catsnap.CatsnapAuthorization.shared.infrastructure.web.response.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.catsnap.CatsnapAuthorization.shared.infrastructure.web.response.ResultCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

/**
 * 공통 에러 코드
 * <p>
 * 모든 도메인에서 공통적으로 발생할 수 있는 에러를 정의합니다.
 */
@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ResultCode {

    /**
     * 존재하지 않는 API 엔드포인트
     */
    NOT_FOUND_API(HttpStatus.NOT_FOUND, "EA000", "존재하지 않는 API입니다."),

    /**
     * 필수 요청 파라미터 누락
     */
    MISSING_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "EA001", "필수 요청 파라미터가 누락되었습니다."),

    /**
     * 잘못된 요청 바디
     */
    INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST, "EA002", "요청 바디가 올바르지 않습니다."),

    /**
     * 서버 내부 오류
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "EA003", "서버 내부 오류가 발생했습니다."),

    /**
     * 잘못된 요청 형식
     */
    INVALID_REQUEST_FORMAT(HttpStatus.BAD_REQUEST, "EA004", "요청 형식이 올바르지 않습니다."),

    /**
     * 메서드 지원 안 함
     */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "EA005", "지원하지 않는 HTTP 메서드입니다."),

    /**
     * 미지원 미디어 타입
     */
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "EA006", "지원하지 않는 미디어 타입입니다."),

    /**
     * 도메인 로직 상 잘못된 값
     */
    DOMAIN_CONSTRAINT_VIOLATION(HttpStatus.BAD_REQUEST, "EA007", "해당 값이 유효하지 않습니다.");

    private final HttpStatusCode httpStatus;
    private final String code;
    private final String message;
}
