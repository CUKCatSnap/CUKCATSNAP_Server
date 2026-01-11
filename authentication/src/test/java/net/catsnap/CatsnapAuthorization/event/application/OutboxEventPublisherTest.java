package net.catsnap.CatsnapAuthorization.event.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.Instant;
import net.catsnap.CatsnapAuthorization.event.domain.Outbox;
import net.catsnap.CatsnapAuthorization.event.infrastrucutre.OutboxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("OutboxEventPublisher 단위 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OutboxEventPublisherTest {

    private OutboxRepository outboxRepository;
    private OutboxEventPublisher publisher;

    @BeforeEach
    void setUp() {
        outboxRepository = mock(OutboxRepository.class);
        publisher = new OutboxEventPublisher(outboxRepository);
    }

    @Test
    void 올바른_파라미터로_Outbox가_생성되어_저장된다() {
        // given
        String aggregateType = "Photographer";
        String aggregateId = "12345";
        String eventType = "PhotographerCreated";
        byte[] eventPayload = new byte[]{1, 2, 3};
        int version = 1;
        Instant timestamp = Instant.parse("2026-01-11T10:00:00Z");
        String correlationId = "correlation-123";
        String causationId = "causation-456";

        ArgumentCaptor<Outbox> captor = ArgumentCaptor.forClass(Outbox.class);

        // when
        publisher.publish(aggregateType, aggregateId, eventType, eventPayload, version,
            timestamp, correlationId, causationId);

        // then
        verify(outboxRepository).save(captor.capture());

        Outbox savedOutbox = captor.getValue();
        assertThat(savedOutbox.getAggregateType()).isEqualTo(aggregateType);
        assertThat(savedOutbox.getAggregateId()).isEqualTo(aggregateId);
        assertThat(savedOutbox.getEventType()).isEqualTo(eventType);
        assertThat(savedOutbox.getPayload()).isEqualTo(eventPayload);
        assertThat(savedOutbox.getVersion()).isEqualTo(version);
        assertThat(savedOutbox.getTimestamp()).isEqualTo(timestamp);
        assertThat(savedOutbox.getCorrelationId()).isEqualTo(correlationId);
        assertThat(savedOutbox.getCausationId()).isEqualTo(causationId);
    }
}