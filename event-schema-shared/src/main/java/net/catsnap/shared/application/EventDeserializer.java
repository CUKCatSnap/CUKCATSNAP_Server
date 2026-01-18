package net.catsnap.shared.application;

/**
 * 이벤트 역직렬화 인터페이스
 *
 * <p>바이너리 데이터를 도메인 이벤트로 역직렬화하기 위한 추상화된 인터페이스입니다.
 * 실제 구현은 Infrastructure 계층에서 제공되며, 내부 역직렬화 기술(Avro, Protobuf 등)은 사용하는 측에서 알 필요가 없습니다.</p>
 *
 */
public interface EventDeserializer {

    /**
     * 바이너리 데이터를 이벤트 객체로 역직렬화합니다.
     *
     * <p>이벤트 스키마로 정의된 모든 이벤트 객체를 역직렬화할 수 있습니다.
     * (예: PhotographerCreated, ReservationCreated 등)</p>
     *
     * @param data        역직렬화할 바이트 배열 (null 불가)
     * @param targetClass 역직렬화할 대상 클래스
     * @param <T>         역직렬화 대상 타입
     * @return 역직렬화된 이벤트 객체
     * @throws IllegalArgumentException data가 null이거나 targetClass가 null인 경우
     */
    <T> T deserialize(byte[] data, Class<T> targetClass);
}