package net.catsnap.CatsnapAuthorization.shared.presentation.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.catsnap.CatsnapAuthorization.shared.domain.error.ResultCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

/**
 * 보안 관련 에러 코드
 * <p>
 * 인증(Authentication) 및 인가(Authorization)와 관련된 에러를 정의합니다.
 * Presentation Layer의 보안 검증에서 발생하는 에러를 담당합니다.
 * </p>
 */
@Getter
@RequiredArgsConstructor
public enum SecurityErrorCode implements ResultCode {

    /**
     * 인증 정보 없음 (401 Unauthorized)
     * <p>
     * 요청에 인증 헤더가 없거나 비어있는 경우
     */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "EA100", "인증 정보가 없습니다."),

    /**
     * 유효하지 않은 권한 정보 (401 Unauthorized)
     * <p>
     * 인증 헤더의 권한 값이 유효하지 않은 경우
     */
    INVALID_AUTHORITY(HttpStatus.UNAUTHORIZED, "EA101", "유효하지 않은 권한 정보입니다."),

    /**
     * 접근 권한 없음 (403 Forbidden)
     * <p>
     * 인증은 되었으나 해당 리소스에 접근할 권한이 없는 경우
     */
    FORBIDDEN(HttpStatus.FORBIDDEN, "EA102", "접근 권한이 없습니다.");

    private final HttpStatusCode httpStatus;
    private final String code;
    private final String message;
}