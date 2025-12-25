package net.catsnap.shared.passport.domain.exception;

/**
 * Passport가 만료된 경우 발생하는 예외입니다.
 * <p>
 * Passport의 유효기간(exp)이 현재 시간보다 이전인 경우 발생합니다.
 * 이는 도메인 규칙 위반에 해당합니다.
 * </p>
 */
public class ExpiredPassportException extends PassportException {

    /**
     * 기본 메시지로 예외를 생성합니다.
     */
    public ExpiredPassportException() {
        super("Passport has expired");
    }

    /**
     * 커스텀 메시지로 예외를 생성합니다.
     *
     * @param message 예외 메시지
     */
    public ExpiredPassportException(String message) {
        super(message);
    }
}