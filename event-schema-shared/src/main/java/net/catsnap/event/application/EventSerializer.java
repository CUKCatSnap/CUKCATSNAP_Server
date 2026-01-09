package net.catsnap.event.application;

import org.apache.avro.specific.SpecificRecordBase;

/**
 * 이벤트 직렬화 인터페이스
 *
 * <p>도메인 이벤트를 바이너리로 직렬화하기 위한 추상화된 인터페이스입니다.
 * 실제 구현은 Infrastructure 계층에서 제공됩니다.</p>
 *
 * @see net.catsnap.event.infrastructure.AvroEventSerializer
 */
public interface EventSerializer {

    /**
     * Avro 이벤트를 바이너리로 직렬화합니다.
     *
     * <p>모든 Avro 생성 이벤트 클래스(SpecificRecordBase 상속)를
     * 범용적으로 직렬화할 수 있습니다.</p>
     *
     * @param event Avro 이벤트 객체 (예: PhotographerCreated, ReservationCreated 등)
     * @param <T>   SpecificRecordBase를 상속받은 Avro 이벤트 타입
     * @return 직렬화된 바이트 배열
     */
    <T extends SpecificRecordBase> byte[] serialize(T event);
}
