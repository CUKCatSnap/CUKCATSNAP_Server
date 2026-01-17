package net.catsnap.CatsnapAuthorization.event.infrastructure;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.function.Consumer;
import net.catsnap.CatsnapAuthorization.event.application.EventRelay;
import net.catsnap.CatsnapAuthorization.event.domain.Outbox;
import net.catsnap.event.shared.EventEnvelope;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Kafka 기반 이벤트 릴레이 구현체
 *
 * <p>Outbox에 저장된 이벤트를 Kafka로 비동기 전송합니다.</p>
 */
@Component
public class KafkaEventRelay implements EventRelay {

    private final KafkaTemplate<String, EventEnvelope> kafkaTemplate;

    public KafkaEventRelay(KafkaTemplate<String, EventEnvelope> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void relay(Outbox outbox, Runnable onSuccess, Consumer<Throwable> onFailure) {
        EventEnvelope envelope = createEventEnvelope(outbox);

        ProducerRecord<String, EventEnvelope> record = new ProducerRecord<>(
            outbox.getEventType(),
            outbox.getAggregateId(),
            envelope
        );

        kafkaTemplate.send(record)
            .whenComplete((result, exception) -> {
                if (exception == null) {
                    onSuccess.run();
                } else {
                    onFailure.accept(exception);
                }
            }
        );
    }

    private EventEnvelope createEventEnvelope(Outbox outbox) {
        return EventEnvelope.newBuilder()
            .setEventId(outbox.getEventId())
            .setEventType(outbox.getEventType())
            .setAggregateId(outbox.getAggregateId())
            .setAggregateType(outbox.getAggregateType())
            .setVersion(outbox.getVersion())
            .setTimestamp(outbox.getTimestamp())
            .setCorrelationId(outbox.getCorrelationId())
            .setCausationId(outbox.getCausationId())
            .setPayload(ByteBuffer.wrap(outbox.getPayload()))
            .setMetadata(new HashMap<>())
            .build();
    }
}
