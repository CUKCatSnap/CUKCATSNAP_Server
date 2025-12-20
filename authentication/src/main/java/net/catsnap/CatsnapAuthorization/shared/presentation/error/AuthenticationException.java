package net.catsnap.CatsnapAuthorization.shared.presentation.error;

import lombok.Getter;

/**
 * 인증 실패 예외 (401 Unauthorized)
 * <p>
 * 인증 정보가 없거나 유효하지 않은 경우 발생합니다.
 * HTTP 401 상태 코드에 매핑됩니다.
 * </p>
 */
@Getter
public class AuthenticationException extends RuntimeException {

    private final SecurityErrorCode errorCode;

    public AuthenticationException(SecurityErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public AuthenticationException(SecurityErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
