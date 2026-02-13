package net.catsnap.CatsnapAuthorization.event.application;

import java.util.List;
import net.catsnap.CatsnapAuthorization.event.domain.Outbox;
import net.catsnap.CatsnapAuthorization.event.domain.OutboxStatus;
import net.catsnap.CatsnapAuthorization.event.infrastructure.OutboxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 아웃박스 릴레이 서비스
 *
 * <p>Outbox 테이블에 저장된 이벤트를 주기적으로 조회하여 외부로 발행합니다.
 * 스케줄러에 의해 주기적으로 호출됩니다.</p>
 */
@Service
public class OutboxRelayService {

    private static final Logger log = LoggerFactory.getLogger(OutboxRelayService.class);
    private static final int MAX_RETRY_COUNT = 3;

    private final OutboxRepository outboxRepository;
    private final EventRelay eventRelay;

    public OutboxRelayService(OutboxRepository outboxRepository, EventRelay eventRelay) {
        this.outboxRepository = outboxRepository;
        this.eventRelay = eventRelay;
    }

    /**
     * 아웃박스에 저장된 PENDING 이벤트를 외부로 발행합니다.
     *
     * <p>발행에 성공하면 Outbox 상태를 PUBLISHED로 변경하고,
     * 실패하면 재시도 횟수를 증가시킵니다.</p>
     */
    @Transactional
    public void relayPendingEvents() {
        List<Outbox> pendingEvents = outboxRepository.findByStatus(OutboxStatus.PENDING);

        if (pendingEvents.isEmpty()) {
            return;
        }

        log.info("릴레이 시작: {} 개의 이벤트 발행 시도", pendingEvents.size());

        for (Outbox outbox : pendingEvents) {
            relayEvent(outbox);
        }
    }

    private void relayEvent(Outbox outbox) {
        eventRelay.relay(
            outbox,
            () -> handleRelaySuccess(outbox),
            throwable -> handleRelayFailure(outbox, throwable)
        );
    }

    private void handleRelaySuccess(Outbox outbox) {
        outbox.markAsPublished();
        outboxRepository.save(outbox);
        log.info("이벤트 발행 성공: eventId={}, eventType={}",
            outbox.getEventId(), outbox.getEventType());
    }

    private void handleRelayFailure(Outbox outbox, Throwable e) {
        log.error("이벤트 발행 실패: eventId={}, eventType={}, error={}",
            outbox.getEventId(), outbox.getEventType(), e.getMessage(), e);

        if (outbox.canRetry(MAX_RETRY_COUNT)) {
            outbox.retry();
            log.warn("재시도 예정: eventId={}, retryCount={}",
                outbox.getEventId(), outbox.getRetryCount());
        } else {
            outbox.markAsFailed(e.getMessage());
            log.error("재시도 횟수 초과: eventId={}, 최종 실패 처리", outbox.getEventId());
        }

        outboxRepository.save(outbox);
    }
}
