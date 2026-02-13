package net.catsnap.CatsnapAuthorization.shared.presentation.error;

import lombok.Getter;

/**
 * 권한 부족 예외 (403 Forbidden)
 * <p>
 * 인증은 성공했으나 해당 리소스에 접근할 권한이 없는 경우 발생합니다.
 * HTTP 403 상태 코드에 매핑됩니다.
 * </p>
 */
@Getter
public class AuthorizationException extends RuntimeException {

    private final SecurityErrorCode errorCode;

    public AuthorizationException(SecurityErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public AuthorizationException(SecurityErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
