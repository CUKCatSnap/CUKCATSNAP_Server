package net.catsnap.CatsnapAuthorization.photographer.infrastructure.event;

import net.catsnap.CatsnapAuthorization.photographer.domain.events.PhotographerCreatedEvent;
import net.catsnap.CatsnapAuthorization.event.application.EventPublisher;
import net.catsnap.event.photographer.v1.PhotographerCreated;
import net.catsnap.shared.application.EventSerializer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Photographer 도메인 이벤트를 생성하는 리스너
 *
 * <p>Spring의 TransactionalEventListener를 사용하여
 * 도메인 트랜잭션 커밋 전에 이벤트를 발행합니다.</p>
 *
 * <p>이를 통해 도메인 변경사항과 이벤트 저장이 같은 트랜잭션에서 원자적으로 처리됩니다.</p>
 */
@Component
public class PhotographerEventListener {

    private final EventPublisher eventPublisher;
    private final EventSerializer eventSerializer;

    public PhotographerEventListener(EventPublisher eventPublisher,
        EventSerializer eventSerializer) {
        this.eventPublisher = eventPublisher;
        this.eventSerializer = eventSerializer;
    }

    /**
     * PhotographerCreatedEvent를 처리하여 아웃박스에 저장합니다.
     *
     * <p>BEFORE_COMMIT 페이즈를 사용하여 도메인 트랜잭션 커밋 전에 실행되므로,
     * Photographer 저장과 이벤트 저장이 같은 트랜잭션에서 처리됩니다.</p>
     *
     * @param event PhotographerCreatedEvent 도메인 이벤트
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handlePhotographerCreatedEvent(PhotographerCreatedEvent event) {
        // 1. 이벤트 생성
        PhotographerCreated eventSchema = PhotographerCreated.newBuilder()
            .setPhotographerId(event.photographerId())
            .build();

        // 2. EventSerializer를 통한 바이너리 직렬화
        byte[] payload = eventSerializer.serialize(eventSchema);

        // 3. 아웃박스에 저장 (EventEnvelope 필드와 함께)
        eventPublisher.publish(
            "Photographer",
            event.photographerId().toString(),
            PhotographerCreated.getClassSchema().getName(),
            payload,
            1,                    // version
            event.timestamp(),    // 이벤트 발생 시점
            null,                 // correlationId
            null                  // causationId
        );
    }
}