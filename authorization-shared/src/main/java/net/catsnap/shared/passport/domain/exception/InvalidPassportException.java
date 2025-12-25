package net.catsnap.shared.passport.domain.exception;

/**
 * Passport가 유효하지 않은 경우 발생하는 예외입니다.
 * <p>
 * 다음과 같은 경우에 발생합니다:
 * <ul>
 *   <li>서명 검증 실패</li>
 *   <li>필수 필드 누락 (userId, authority 등)</li>
 *   <li>잘못된 형식의 Passport</li>
 *   <li>알 수 없는 버전의 Passport</li>
 * </ul>
 * </p>
 */
public class InvalidPassportException extends PassportException {

    /**
     * 기본 메시지로 예외를 생성합니다.
     */
    public InvalidPassportException() {
        super("Invalid passport");
    }

    /**
     * 커스텀 메시지로 예외를 생성합니다.
     *
     * @param message 예외 메시지
     */
    public InvalidPassportException(String message) {
        super(message);
    }

    /**
     * 메시지와 원인 예외를 포함한 예외를 생성합니다.
     *
     * @param message 예외 메시지
     * @param cause   원인 예외
     */
    public InvalidPassportException(String message, Throwable cause) {
        super(message, cause);
    }
}