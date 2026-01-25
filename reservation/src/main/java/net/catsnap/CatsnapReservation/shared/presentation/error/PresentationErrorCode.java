package net.catsnap.CatsnapReservation.shared.presentation.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.catsnap.CatsnapReservation.shared.ResultCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

/**
 * 프레젠테이션 레이어 에러 코드
 * <p>
 * API 요청/응답 처리 과정에서 발생하는 에러를 정의합니다.
 * 잘못된 요청 형식, 존재하지 않는 엔드포인트, 인증/인가 실패 등 HTTP 레벨의 에러를 담당합니다.
 * </p>
 */
@Getter
@RequiredArgsConstructor
public enum PresentationErrorCode implements ResultCode {

    // ========== API 요청 관련 에러 (EA0XX) ==========

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

    // ========== 인증/인가 관련 에러 (EA1XX) ==========

    /**
     * 인증 정보 없음 (401 Unauthorized)
     * <p>
     * 요청에 인증 헤더가 없거나 비어있는 경우
     * </p>
     */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "EA100", "인증 정보가 없습니다."),

    /**
     * 유효하지 않은 권한 정보 (401 Unauthorized)
     * <p>
     * 인증 헤더의 권한 값이 유효하지 않은 경우
     * </p>
     */
    INVALID_AUTHORITY(HttpStatus.UNAUTHORIZED, "EA101", "유효하지 않은 권한 정보입니다."),

    /**
     * 접근 권한 없음 (403 Forbidden)
     * <p>
     * 인증은 되었으나 해당 리소스에 접근할 권한이 없는 경우
     * </p>
     */
    FORBIDDEN(HttpStatus.FORBIDDEN, "EA102", "접근 권한이 없습니다."),

    /**
     * 유효하지 않은 Passport (401 Unauthorized)
     * <p>
     * Passport 서명 검증에 실패했거나 파싱할 수 없는 경우
     * </p>
     */
    INVALID_PASSPORT(HttpStatus.UNAUTHORIZED, "EA103", "유효하지 않은 Passport입니다."),

    /**
     * 만료된 Passport (401 Unauthorized)
     * <p>
     * Passport의 유효기간이 만료된 경우
     * </p>
     */
    EXPIRED_PASSPORT(HttpStatus.UNAUTHORIZED, "EA104", "만료된 Passport입니다.");

    private final HttpStatusCode httpStatus;
    private final String code;
    private final String message;
}
