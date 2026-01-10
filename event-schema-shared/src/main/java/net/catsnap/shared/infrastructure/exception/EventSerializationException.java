package net.catsnap.shared.infrastructure.exception;

/**
 * 이벤트 직렬화 중 오류가 발생한 경우 발생하는 예외입니다.
 * <p>
 * 다음과 같은 경우에 발생합니다:
 * <ul>
 *   <li>Avro 스키마 기반 바이너리 인코딩 실패</li>
 *   <li>이벤트 객체의 필드 값이 잘못된 경우</li>
 *   <li>I/O 오류로 인한 직렬화 실패</li>
 * </ul>
 * </p>
 */
public class EventSerializationException extends EventProcessingException {

    /**
     * 기본 메시지로 예외를 생성합니다.
     *
     * @param cause 원인 예외
     */
    public EventSerializationException(Throwable cause) {
        super("이벤트 직렬화에 실패했습니다.", cause);
    }

    /**
     * 커스텀 메시지와 원인 예외를 포함한 예외를 생성합니다.
     *
     * @param message 예외 메시지
     * @param cause   원인 예외
     */
    public EventSerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 커스텀 메시지로 예외를 생성합니다.
     *
     * @param message 예외 메시지
     */
    public EventSerializationException(String message) {
        super(message);
    }
}
