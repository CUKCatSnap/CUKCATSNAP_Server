package net.catsnap.CatsnapAuthorization.event.presentation;

import net.catsnap.CatsnapAuthorization.event.application.OutboxRelayService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 아웃박스 릴레이 스케줄러
 *
 * <p>주기적으로 OutboxRelayService를 호출하여
 * 아웃박스 테이블의 PENDING 이벤트를 발행합니다.</p>
 *
 * <p>예외 발생 시 글로벌 ErrorHandler(SchedulingConfig)에서 처리합니다.</p>
 */
@Component
public class OutboxRelayScheduler {

    private final OutboxRelayService outboxRelayService;

    public OutboxRelayScheduler(OutboxRelayService outboxRelayService) {
        this.outboxRelayService = outboxRelayService;
    }

    /**
     * 5초마다 아웃박스 이벤트를 릴레이합니다.
     *
     * <p>fixedDelay를 사용하여 이전 실행이 완료된 후 5초 대기합니다.</p>
     *
     * <p>initialDelay = 10000: 애플리케이션 시작 후 '10초' 뒤에 첫 실행을 시작합니다.</p>
     * <p>fixedDelay = 5000: 이전 작업이 '종료된 시점'으로부터 '5초' 후에 다음 작업을 실행합니다.</p>
     */
    @Scheduled(fixedDelay = 5000, initialDelay = 10000)
    public void relayOutboxEvents() {
        outboxRelayService.relayPendingEvents();
    }
}
