package net.catsnap.shared.passport.domain.exception;

/**
 * Passport 파싱 중 오류가 발생한 경우 발생하는 예외입니다.
 * <p>
 * 다음과 같은 경우에 발생합니다:
 * <ul>
 *   <li>Base64 디코딩 실패</li>
 *   <li>바이트 배열 역직렬화 실패</li>
 *   <li>잘못된 바이트 길이</li>
 *   <li>손상된 Passport 데이터</li>
 * </ul>
 * </p>
 */
public class PassportParsingException extends PassportException {

    /**
     * 기본 메시지로 예외를 생성합니다.
     */
    public PassportParsingException() {
        super("Failed to parse passport");
    }

    /**
     * 커스텀 메시지로 예외를 생성합니다.
     *
     * @param message 예외 메시지
     */
    public PassportParsingException(String message) {
        super(message);
    }

    /**
     * 메시지와 원인 예외를 포함한 예외를 생성합니다.
     *
     * @param message 예외 메시지
     * @param cause   원인 예외 (Base64 디코딩 예외, ByteBuffer 예외 등)
     */
    public PassportParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}