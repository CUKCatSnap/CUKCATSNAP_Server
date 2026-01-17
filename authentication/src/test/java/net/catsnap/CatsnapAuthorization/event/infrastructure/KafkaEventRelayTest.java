package net.catsnap.CatsnapAuthorization.event.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import net.catsnap.CatsnapAuthorization.event.domain.Outbox;
import net.catsnap.event.shared.EventEnvelope;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

@DisplayName("KafkaEventRelay 단위 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings({"NonAsciiCharacters", "unchecked"})
class KafkaEventRelayTest {

    private KafkaTemplate<String, EventEnvelope> kafkaTemplate;
    private KafkaEventRelay kafkaEventRelay;

    @BeforeEach
    void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        kafkaEventRelay = new KafkaEventRelay(kafkaTemplate);
    }

    @Test
    void 발행_성공_시_onSuccess_콜백이_호출된다() {
        // given
        Outbox outbox = createTestOutbox();
        CompletableFuture<SendResult<String, EventEnvelope>> future = new CompletableFuture<>();
        when(kafkaTemplate.send(any(ProducerRecord.class))).thenReturn(future);

        AtomicBoolean successCalled = new AtomicBoolean(false);
        AtomicReference<Throwable> failureException = new AtomicReference<>();

        // when
        kafkaEventRelay.relay(
            outbox,
            () -> successCalled.set(true),
            failureException::set
        );
        future.complete(mock(SendResult.class));

        // then
        assertThat(successCalled.get()).isTrue();
        assertThat(failureException.get()).isNull();
    }

    @Test
    void 발행_실패_시_onFailure_콜백이_호출된다() {
        // given
        Outbox outbox = createTestOutbox();
        CompletableFuture<SendResult<String, EventEnvelope>> future = new CompletableFuture<>();
        when(kafkaTemplate.send(any(ProducerRecord.class))).thenReturn(future);

        AtomicBoolean successCalled = new AtomicBoolean(false);
        AtomicReference<Throwable> failureException = new AtomicReference<>();
        RuntimeException expectedException = new RuntimeException("Kafka 전송 실패");

        // when
        kafkaEventRelay.relay(
            outbox,
            () -> successCalled.set(true),
            failureException::set
        );
        future.completeExceptionally(expectedException);

        // then
        assertThat(successCalled.get()).isFalse();
        assertThat(failureException.get()).isEqualTo(expectedException);
    }

    @Test
    void ProducerRecord가_올바른_값으로_생성된다() {
        // given
        Outbox outbox = createTestOutbox();
        CompletableFuture<SendResult<String, EventEnvelope>> future = new CompletableFuture<>();
        ArgumentCaptor<ProducerRecord<String, EventEnvelope>> captor =
            ArgumentCaptor.forClass(ProducerRecord.class);
        when(kafkaTemplate.send(captor.capture())).thenReturn(future);

        // when
        kafkaEventRelay.relay(outbox, () -> {}, e -> {});
        future.complete(mock(SendResult.class));

        // then
        ProducerRecord<String, EventEnvelope> record = captor.getValue();
        assertThat(record.topic()).isEqualTo(outbox.getEventType());
        assertThat(record.key()).isEqualTo(outbox.getAggregateId());

        EventEnvelope envelope = record.value();
        assertThat(envelope.getEventId()).isEqualTo(outbox.getEventId());
        assertThat(envelope.getEventType()).isEqualTo(outbox.getEventType());
        assertThat(envelope.getAggregateId()).isEqualTo(outbox.getAggregateId());
        assertThat(envelope.getAggregateType()).isEqualTo(outbox.getAggregateType());
        assertThat(envelope.getVersion()).isEqualTo(outbox.getVersion());
        assertThat(envelope.getTimestamp()).isEqualTo(outbox.getTimestamp());
        assertThat(envelope.getCorrelationId()).isEqualTo(outbox.getCorrelationId());
        assertThat(envelope.getCausationId()).isEqualTo(outbox.getCausationId());
    }

    private Outbox createTestOutbox() {
        return Outbox.prepareForPublishing(
            "Photographer",
            "photographer-123",
            "PhotographerCreated",
            new byte[]{1, 2, 3},
            1,
            Instant.parse("2026-01-18T10:00:00Z"),
            "correlation-id",
            "causation-id"
        );
    }
}