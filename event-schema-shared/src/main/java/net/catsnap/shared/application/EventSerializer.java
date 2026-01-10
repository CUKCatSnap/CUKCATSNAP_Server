package net.catsnap.shared.application;

/**
 * 이벤트 직렬화 인터페이스
 *
 * <p>도메인 이벤트를 바이너리로 직렬화하기 위한 추상화된 인터페이스입니다.
 * 실제 구현은 Infrastructure 계층에서 제공되며, 내부 직렬화 기술(Avro, Protobuf 등)은 사용하는 측에서 알 필요가 없습니다.</p>
 *
 */
public interface EventSerializer {

    /**
     * 이벤트 객체를 바이너리로 직렬화합니다.
     *
     * <p>이벤트 스키마로 정의된 모든 이벤트 객체를 직렬화할 수 있습니다.
     * (예: PhotographerCreated, ReservationCreated 등)</p>
     *
     * @param event 직렬화할 이벤트 객체 (null 불가)
     * @return 직렬화된 바이트 배열
     * @throws IllegalArgumentException event가 null이거나 지원되지 않는 타입인 경우
     */
    byte[] serialize(Object event);
}
