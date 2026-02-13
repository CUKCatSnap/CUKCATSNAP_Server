package net.catsnap.shared.passport.domain.exception;

/**
 * Passport 관련 예외의 공통 추상 클래스입니다.
 * <p>
 * 모든 Passport 도메인 예외는 이 클래스를 상속받아야 합니다.
 * Passport의 파싱, 검증, 만료 등 모든 예외 상황을 포괄합니다.
 * </p>
 */
public abstract class PassportException extends RuntimeException {

    /**
     * 메시지를 포함한 예외를 생성합니다.
     *
     * @param message 예외 메시지
     */
    protected PassportException(String message) {
        super(message);
    }

    /**
     * 메시지와 원인 예외를 포함한 예외를 생성합니다.
     *
     * @param message 예외 메시지
     * @param cause   원인 예외
     */
    protected PassportException(String message, Throwable cause) {
        super(message, cause);
    }
}