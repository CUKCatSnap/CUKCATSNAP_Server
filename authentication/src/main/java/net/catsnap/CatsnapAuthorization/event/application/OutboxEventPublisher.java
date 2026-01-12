package net.catsnap.CatsnapAuthorization.event.application;

import java.time.Instant;
import net.catsnap.CatsnapAuthorization.event.domain.Outbox;
import net.catsnap.CatsnapAuthorization.event.infrastructure.OutboxRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 아웃박스 패턴 기반 이벤트 발행 구현체
 *
 * <p>EventPublisher 인터페이스를 구현하며, 이벤트를 Outbox 테이블에 저장합니다.
 * 실제 Kafka 발행은 별도의 릴레이 프로세스가 담당합니다.</p>
 */
@Component
public class OutboxEventPublisher implements EventPublisher {

    private final OutboxRepository outboxRepository;

    public OutboxEventPublisher(OutboxRepository outboxRepository) {
        this.outboxRepository = outboxRepository;
    }

    /**
     * 이벤트를 Outbox 테이블에 저장합니다.
     *
     * <p>도메인 트랜잭션과 동일한 트랜잭션 내에서 실행되어야합니다.
     * 이를 통해 도메인 변경사항과 이벤트 저장이 원자적으로 처리됩니다.</p>
     *
     * @param aggregateType 애그리거트 타입
     * @param aggregateId   애그리거트 ID
     * @param eventType     이벤트 타입
     * @param eventPayload  직렬화된 이벤트 페이로드
     * @param version       스키마 버전
     * @param timestamp     이벤트 발생 시점
     * @param correlationId 이벤트들의 루트 이벤트 ID
     * @param causationId   해당 이벤트를 트리거한 이벤트 ID
     */
    @Override
    @Transactional
    public void publish(String aggregateType, String aggregateId, String eventType,
        byte[] eventPayload, int version, Instant timestamp, String correlationId,
        String causationId) {
        Outbox outbox = Outbox.prepareForPublishing(
            aggregateType,
            aggregateId,
            eventType,
            eventPayload,
            version,
            timestamp,
            correlationId,
            causationId
        );

        outboxRepository.save(outbox);
    }
}