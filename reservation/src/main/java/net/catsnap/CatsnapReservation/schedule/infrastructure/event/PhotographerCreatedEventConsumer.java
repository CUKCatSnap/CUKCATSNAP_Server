package net.catsnap.CatsnapReservation.schedule.infrastructure.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.catsnap.CatsnapReservation.schedule.application.PhotographerScheduleService;
import net.catsnap.event.photographer.v1.PhotographerCreated;
import net.catsnap.event.shared.EventEnvelope;
import net.catsnap.shared.application.EventDeserializer;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

/**
 * PhotographerCreated 이벤트 Kafka Consumer
 *
 * <p>member-service에서 사진작가가 생성되면 해당 이벤트를 수신하여
 * 기본 스케줄을 생성합니다.</p>
 *
 * <h3>재시도 정책</h3>
 * <ul>
 *   <li>최대 3회 시도 (원본 1회 + 재시도 2회)</li>
 *   <li>재시도 간격: 5초 → 10초 (exponential backoff)</li>
 *   <li>최종 실패 시 DLT(Dead Letter Topic)로 이동</li>
 * </ul>
 *
 * <h3>생성되는 토픽</h3>
 * <ul>
 *   <li>PhotographerCreated (원본)</li>
 *   <li>PhotographerCreated-retry-0 (5초 후 재시도)</li>
 *   <li>PhotographerCreated-retry-1 (10초 후 재시도)</li>
 *   <li>PhotographerCreated-dlt (최종 실패)</li>
 * </ul>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PhotographerCreatedEventConsumer {

    private final PhotographerScheduleService photographerScheduleService;
    private final EventDeserializer eventDeserializer;

    /**
     * PhotographerCreated 이벤트를 처리합니다.
     *
     * @param envelope Kafka로부터 수신한 이벤트 envelope
     */
    @RetryableTopic(
        attempts = "3",
        backoff = @Backoff(delay = 5000, multiplier = 2),
        dltStrategy = DltStrategy.FAIL_ON_ERROR,
        autoCreateTopics = "true"
    )
    @KafkaListener(topics = "PhotographerCreated", groupId = "schedule-create")
    public void consume(EventEnvelope envelope) {
        log.info("Received PhotographerCreated event: eventId={}, aggregateId={}",
            envelope.getEventId(), envelope.getAggregateId());

        PhotographerCreated event = eventDeserializer.deserialize(
            envelope.getPayload().array(),
            PhotographerCreated.class
        );

        photographerScheduleService.createDefaultSchedule(event.getPhotographerId());
    }

    /**
     * DLT(Dead Letter Topic)로 이동된 메시지를 처리합니다.
     *
     * <p>재시도 횟수를 모두 소진한 메시지가 이 핸들러로 전달됩니다.
     * 현재는 로깅만 수행하며, 추후 알림 연동이 필요합니다.</p>
     *
     * @param envelope 처리 실패한 이벤트 envelope
     * @param errorMessage 실패 원인 메시지
     */
    @DltHandler
    public void handleDlt(EventEnvelope envelope,
                          @Header(KafkaHeaders.EXCEPTION_MESSAGE) String errorMessage) {
        log.error("DLT 이동 - eventId: {}, aggregateId: {}, error: {}",
            envelope.getEventId(), envelope.getAggregateId(), errorMessage);
        // TODO: Slack/Discord 알림 연동
    }
}
