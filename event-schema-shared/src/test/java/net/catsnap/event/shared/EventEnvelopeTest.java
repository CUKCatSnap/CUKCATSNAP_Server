package net.catsnap.event.shared;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

/**
 * EventEnvelope 스키마 직렬화/역직렬화 및 호환성 테스트
 */
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class EventEnvelopeTest {

    @Test
    void EventEnvelope_기본_생성_및_필드_검증() {
        // given
        String eventId = "event-123";
        String eventType = "PhotographerCreated";
        String aggregateId = "photographer-456";
        String aggregateType = "Photographer";
        long timestamp = System.currentTimeMillis();
        byte[] payload = "test payload".getBytes();

        Map<String, String> metadata = new HashMap<>();
        metadata.put("userId", "user-789");
        metadata.put("sourceService", "photographer-service");

        // when
        EventEnvelope envelope = EventEnvelope.newBuilder()
                .setEventId(eventId)
                .setEventType(eventType)
                .setAggregateId(aggregateId)
                .setAggregateType(aggregateType)
                .setVersion(1)
                .setTimestamp(timestamp)
                .setCorrelationId("correlation-123")
                .setCausationId("causation-456")
                .setPayload(ByteBuffer.wrap(payload))
                .setMetadata(metadata)
                .build();

        // then
        assertThat(envelope.getEventId()).isEqualTo(eventId);
        assertThat(envelope.getEventType()).isEqualTo(eventType);
        assertThat(envelope.getAggregateId()).isEqualTo(aggregateId);
        assertThat(envelope.getAggregateType()).isEqualTo(aggregateType);
        assertThat(envelope.getVersion()).isEqualTo(1);
        assertThat(envelope.getTimestamp()).isEqualTo(timestamp);
        assertThat(envelope.getCorrelationId()).isEqualTo("correlation-123");
        assertThat(envelope.getCausationId()).isEqualTo("causation-456");
        assertThat(envelope.getPayload().array()).isEqualTo(payload);
        assertThat(envelope.getMetadata()).hasSize(2);
        assertThat(envelope.getMetadata().get("userId")).isEqualTo("user-789");
    }

    @Test
    void EventEnvelope_null_필드_처리() {
        // given & when
        EventEnvelope envelope = EventEnvelope.newBuilder()
                .setEventId("event-123")
                .setEventType("TestEvent")
                .setAggregateId("aggregate-456")
                .setAggregateType("TestAggregate")
                .setVersion(1)
                .setTimestamp(System.currentTimeMillis())
                .setCorrelationId(null)
                .setCausationId(null)
                .setPayload(ByteBuffer.wrap("test".getBytes()))
                .setMetadata(new HashMap<>())
                .build();

        // then
        assertThat(envelope.getCorrelationId()).isNull();
        assertThat(envelope.getCausationId()).isNull();
        assertThat(envelope.getMetadata()).isEmpty();
    }

    @Test
    void EventEnvelope_Avro_직렬화_및_역직렬화() throws IOException {
        // given
        EventEnvelope original = createSampleEventEnvelope();

        // when
        byte[] serialized = serializeEventEnvelope(original);
        EventEnvelope deserialized = deserializeEventEnvelope(serialized);

        // then
        assertThat(deserialized.getEventId()).isEqualTo(original.getEventId());
        assertThat(deserialized.getEventType()).isEqualTo(original.getEventType());
        assertThat(deserialized.getAggregateId()).isEqualTo(original.getAggregateId());
        assertThat(deserialized.getAggregateType()).isEqualTo(original.getAggregateType());
        assertThat(deserialized.getVersion()).isEqualTo(original.getVersion());
        assertThat(deserialized.getTimestamp()).isEqualTo(original.getTimestamp());
        assertThat(deserialized.getPayload()).isEqualTo(original.getPayload());
        assertThat(deserialized.getMetadata()).isEqualTo(original.getMetadata());
    }

    @Test
    void EventEnvelope_스키마_호환성_검증() {
        // given
        EventEnvelope envelope = createSampleEventEnvelope();

        // when & then
        assertNotNull(envelope.getSchema());
        assertThat(envelope.getSchema().getName()).isEqualTo("EventEnvelope");
        assertThat(envelope.getSchema().getNamespace()).isEqualTo("net.catsnap.event.shared");
        assertThat(envelope.getSchema().getFields()).hasSize(10);
    }

    @Test
    void 대용량_payload_직렬화_테스트() throws IOException {
        // given
        byte[] largePayload = new byte[10 * 1024]; // 10KB
        for (int i = 0; i < largePayload.length; i++) {
            largePayload[i] = (byte) (i % 256);
        }

        EventEnvelope envelope = EventEnvelope.newBuilder()
                .setEventId("event-large")
                .setEventType("LargeEvent")
                .setAggregateId("aggregate-large")
                .setAggregateType("LargeAggregate")
                .setVersion(1)
                .setTimestamp(System.currentTimeMillis())
                .setPayload(ByteBuffer.wrap(largePayload))
                .setMetadata(new HashMap<>())
                .build();

        // when
        byte[] serialized = serializeEventEnvelope(envelope);
        EventEnvelope deserialized = deserializeEventEnvelope(serialized);

        // then
        assertThat(deserialized.getPayload().array()).isEqualTo(largePayload);
    }

    // Helper methods
    private EventEnvelope createSampleEventEnvelope() {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("userId", "user-123");
        metadata.put("sourceService", "test-service");

        return EventEnvelope.newBuilder()
                .setEventId("evt-12345")
                .setEventType("TestEvent")
                .setAggregateId("agg-67890")
                .setAggregateType("TestAggregate")
                .setVersion(1)
                .setTimestamp(1234567890000L)
                .setCorrelationId("corr-123")
                .setCausationId("cause-456")
                .setPayload(ByteBuffer.wrap("sample payload".getBytes()))
                .setMetadata(metadata)
                .build();
    }

    private byte[] serializeEventEnvelope(EventEnvelope envelope) throws IOException {
        SpecificDatumWriter<EventEnvelope> writer = new SpecificDatumWriter<>(EventEnvelope.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);
        writer.write(envelope, encoder);
        encoder.flush();
        return outputStream.toByteArray();
    }

    private EventEnvelope deserializeEventEnvelope(byte[] data) throws IOException {
        SpecificDatumReader<EventEnvelope> reader = new SpecificDatumReader<>(EventEnvelope.class);
        BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(data, null);
        return reader.read(null, decoder);
    }
}
