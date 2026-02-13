package net.catsnap.shared.infrastructure.kafka;

import static org.assertj.core.api.Assertions.assertThat;

import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import net.catsnap.event.shared.EventEnvelope;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("KafkaValueDeserializer 통합 테스트")
class KafkaValueDeserializerTest {

    private static final String TOPIC = "test-events";
    private SchemaRegistryClient schemaRegistry;
    private KafkaValueSerializer serializer;
    private KafkaValueDeserializer deserializer;

    @BeforeEach
    void setUp() {
        schemaRegistry = new MockSchemaRegistryClient();

        Map<String, Object> config = new HashMap<>();
        config.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "mock://test");
        config.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);

        serializer = new TestableKafkaValueSerializer(schemaRegistry);
        serializer.configure(config, false);

        deserializer = new TestableKafkaValueDeserializer(schemaRegistry);
        deserializer.configure(config, false);
    }

    @Nested
    class deserialize_테스트 {

        @Test
        void EventEnvelope을_역직렬화할_수_있다() {
            // given
            EventEnvelope original = createSampleEventEnvelope();
            byte[] serialized = serializer.serialize(TOPIC, original);

            // when
            EventEnvelope deserialized = deserializer.deserialize(TOPIC, serialized);

            // then
            assertThat(deserialized).isNotNull();
            assertThat(deserialized.getEventId()).isEqualTo(original.getEventId());
            assertThat(deserialized.getEventType()).isEqualTo(original.getEventType());
            assertThat(deserialized.getAggregateId()).isEqualTo(original.getAggregateId());
            assertThat(deserialized.getAggregateType()).isEqualTo(original.getAggregateType());
        }

        @Test
        void 직렬화_역직렬화_round_trip이_정상_동작한다() {
            // given
            EventEnvelope original = createSampleEventEnvelope();

            // when
            byte[] serialized = serializer.serialize(TOPIC, original);
            EventEnvelope deserialized = deserializer.deserialize(TOPIC, serialized);

            // then
            assertThat(deserialized.getEventId()).isEqualTo(original.getEventId());
            assertThat(deserialized.getEventType()).isEqualTo(original.getEventType());
            assertThat(deserialized.getAggregateId()).isEqualTo(original.getAggregateId());
            assertThat(deserialized.getAggregateType()).isEqualTo(original.getAggregateType());
            assertThat(deserialized.getVersion()).isEqualTo(original.getVersion());
            assertThat(deserialized.getTimestamp()).isEqualTo(original.getTimestamp());
            assertThat(deserialized.getCorrelationId()).isEqualTo(original.getCorrelationId());
            assertThat(deserialized.getCausationId()).isEqualTo(original.getCausationId());
        }

        @Test
        void null_데이터는_null을_반환한다() {
            // when
            EventEnvelope result = deserializer.deserialize(TOPIC, null);

            // then
            assertThat(result).isNull();
        }

        @Test
        void null_필드를_포함한_EventEnvelope을_역직렬화할_수_있다() {
            // given
            EventEnvelope original = EventEnvelope.newBuilder()
                .setEventId("event-null-test")
                .setEventType("NullFieldTest")
                .setAggregateId("agg-null")
                .setAggregateType("TestAggregate")
                .setVersion(1)
                .setTimestamp(Instant.now().truncatedTo(ChronoUnit.MILLIS))
                .setCorrelationId(null)
                .setCausationId(null)
                .setPayload(ByteBuffer.wrap("test".getBytes()))
                .setMetadata(new HashMap<>())
                .build();

            // when
            byte[] serialized = serializer.serialize(TOPIC, original);
            EventEnvelope deserialized = deserializer.deserialize(TOPIC, serialized);

            // then
            assertThat(deserialized.getCorrelationId()).isNull();
            assertThat(deserialized.getCausationId()).isNull();
            assertThat(deserialized.getMetadata()).isEmpty();
        }

        @Test
        void metadata를_포함한_EventEnvelope을_역직렬화할_수_있다() {
            // given
            Map<String, String> metadata = new HashMap<>();
            metadata.put("userId", "user-123");
            metadata.put("sourceService", "test-service");

            EventEnvelope original = EventEnvelope.newBuilder()
                .setEventId("event-metadata-test")
                .setEventType("MetadataTest")
                .setAggregateId("agg-metadata")
                .setAggregateType("TestAggregate")
                .setVersion(1)
                .setTimestamp(Instant.now().truncatedTo(ChronoUnit.MILLIS))
                .setPayload(ByteBuffer.wrap("test".getBytes()))
                .setMetadata(metadata)
                .build();

            // when
            byte[] serialized = serializer.serialize(TOPIC, original);
            EventEnvelope deserialized = deserializer.deserialize(TOPIC, serialized);

            // then
            assertThat(deserialized.getMetadata()).hasSize(2);
            assertThat(deserialized.getMetadata().get("userId")).isEqualTo("user-123");
            assertThat(deserialized.getMetadata().get("sourceService")).isEqualTo("test-service");
        }
    }

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
            .setTimestamp(Instant.ofEpochMilli(1234567890000L))
            .setCorrelationId("corr-123")
            .setCausationId("cause-456")
            .setPayload(ByteBuffer.wrap("sample payload".getBytes()))
            .setMetadata(metadata)
            .build();
    }

    /**
     * 테스트용 KafkaValueSerializer (MockSchemaRegistryClient 주입 가능)
     */
    private static class TestableKafkaValueSerializer extends KafkaValueSerializer {
        private final io.confluent.kafka.serializers.KafkaAvroSerializer testSerializer;

        TestableKafkaValueSerializer(SchemaRegistryClient schemaRegistry) {
            this.testSerializer = new io.confluent.kafka.serializers.KafkaAvroSerializer(schemaRegistry);
        }

        @Override
        public void configure(Map<String, ?> configs, boolean isKey) {
            testSerializer.configure(configs, isKey);
        }

        @Override
        public byte[] serialize(String topic, EventEnvelope data) {
            if (data == null) {
                throw new IllegalArgumentException("EventEnvelope 'data' must not be null when serializing for topic '" + topic + "'.");
            }
            return testSerializer.serialize(topic, data);
        }

        @Override
        public void close() {
            testSerializer.close();
        }
    }

    /**
     * 테스트용 KafkaValueDeserializer (MockSchemaRegistryClient 주입 가능)
     */
    private static class TestableKafkaValueDeserializer extends KafkaValueDeserializer {
        private final io.confluent.kafka.serializers.KafkaAvroDeserializer testDeserializer;

        TestableKafkaValueDeserializer(SchemaRegistryClient schemaRegistry) {
            this.testDeserializer = new io.confluent.kafka.serializers.KafkaAvroDeserializer(schemaRegistry);
        }

        @Override
        public void configure(Map<String, ?> configs, boolean isKey) {
            testDeserializer.configure(configs, isKey);
        }

        @Override
        public EventEnvelope deserialize(String topic, byte[] data) {
            if (data == null) {
                return null;
            }
            return (EventEnvelope) testDeserializer.deserialize(topic, data);
        }

        @Override
        public void close() {
            testDeserializer.close();
        }
    }
}