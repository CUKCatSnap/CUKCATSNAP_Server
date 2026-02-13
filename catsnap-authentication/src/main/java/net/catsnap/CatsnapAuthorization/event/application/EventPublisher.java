package net.catsnap.CatsnapAuthorization.event.application;

import java.time.Instant;

/**
 * 도메인 이벤트 발행 인터페이스
 *
 * <p>Application 계층에서 도메인 이벤트를 발행하기 위한 추상화된 인터페이스입니다.
 * EventEnvelope 패턴을 따라 이벤트 메타데이터와 함께 발행합니다.</p>
 */
public interface EventPublisher {

    /**
     * 도메인 이벤트를 발행합니다.
     *
     * <p>EventEnvelope 필드를 받아서 Outbox에 저장하여
     * 이벤트 발생 시점의 정보를 정확히 보존하고 멱등성을 보장합니다.</p>
     *
     * @param aggregateType 애그리거트 타입 (예: "Photographer")
     * @param aggregateId   애그리거트 ID
     * @param eventType     이벤트 타입 (예: "PhotographerCreated")
     * @param eventPayload  직렬화된 이벤트 페이로드
     * @param version       스키마 버전
     * @param timestamp     이벤트 발생 시점
     * @param correlationId 상관관계 ID (분산 추적용, null 가능)
     * @param causationId   인과관계 ID (이벤트 체인 추적용, null 가능)
     */
    void publish(String aggregateType, String aggregateId, String eventType, byte[] eventPayload,
        int version, Instant timestamp, String correlationId, String causationId);
}