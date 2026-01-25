package net.catsnap.CatsnapReservation.schedule.infrastructure.event;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import net.catsnap.CatsnapReservation.schedule.application.PhotographerScheduleService;
import net.catsnap.event.photographer.v1.PhotographerCreated;
import net.catsnap.event.shared.EventEnvelope;
import net.catsnap.shared.application.EventDeserializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("PhotographerCreatedEventConsumer 단위 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class PhotographerCreatedEventConsumerTest {

    @InjectMocks
    private PhotographerCreatedEventConsumer consumer;

    @Mock
    private PhotographerScheduleService photographerScheduleService;

    @Mock
    private EventDeserializer eventDeserializer;

    @Test
    void 이벤트를_수신하면_스케줄_생성_서비스를_호출한다() {
        // given
        Long photographerId = 1L;
        EventEnvelope envelope = createEventEnvelope("event-1", "1");
        PhotographerCreated event = new PhotographerCreated(photographerId);

        given(eventDeserializer.deserialize(any(byte[].class), eq(PhotographerCreated.class)))
            .willReturn(event);

        // when
        consumer.consume(envelope);

        // then
        then(photographerScheduleService).should().createDefaultSchedule(photographerId);
    }

    @Test
    void 이벤트_페이로드를_역직렬화한다() {
        // given
        Long photographerId = 1L;
        byte[] payloadBytes = "test-payload".getBytes();
        EventEnvelope envelope = createEventEnvelope("event-1", "1", payloadBytes);
        PhotographerCreated event = new PhotographerCreated(photographerId);

        given(eventDeserializer.deserialize(any(byte[].class), eq(PhotographerCreated.class)))
            .willReturn(event);

        // when
        consumer.consume(envelope);

        // then
        then(eventDeserializer).should()
            .deserialize(argThat(bytes -> Arrays.equals(bytes, payloadBytes)), eq(PhotographerCreated.class));
    }

    @Test
    void DLT_핸들러가_예외_없이_실행된다() {
        // given
        EventEnvelope envelope = createEventEnvelope("event-1", "1");
        String errorMessage = "Test error message";

        // when & then (예외 없이 실행되면 성공)
        consumer.handleDlt(envelope, errorMessage);

        // 서비스는 호출되지 않음
        then(photographerScheduleService).should(never()).createDefaultSchedule(any());
    }

    private EventEnvelope createEventEnvelope(String eventId, String aggregateId) {
        return createEventEnvelope(eventId, aggregateId, "payload".getBytes());
    }

    private EventEnvelope createEventEnvelope(String eventId, String aggregateId, byte[] payload) {
        return EventEnvelope.newBuilder()
            .setEventId(eventId)
            .setEventType("PhotographerCreated")
            .setAggregateId(aggregateId)
            .setAggregateType("Photographer")
            .setVersion(1)
            .setTimestamp(Instant.now())
            .setPayload(ByteBuffer.wrap(payload))
            .setMetadata(Map.of())
            .build();
    }
}
