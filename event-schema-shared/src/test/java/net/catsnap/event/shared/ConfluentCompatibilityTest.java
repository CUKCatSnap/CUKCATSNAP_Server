package net.catsnap.event.shared;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

/**
 * Confluent Schema Registry 호환성 테스트
 * - Kafka Avro Serializer/Deserializer 사용
 * - Mock Schema Registry를 사용한 스키마 등록 및 호환성 검증
 */
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ConfluentCompatibilityTest {

    private static final String TOPIC = "test-events";
    private SchemaRegistryClient schemaRegistry;
    private KafkaAvroSerializer serializer;
    private KafkaAvroDeserializer deserializer;

    @BeforeEach
    void setUp() {
        // Mock Schema Registry 설정
        schemaRegistry = new MockSchemaRegistryClient();

        // Serializer/Deserializer 설정
        Map<String, Object> config = new HashMap<>();
        config.put("schema.registry.url", "mock://test");

        serializer = new KafkaAvroSerializer(schemaRegistry);
        serializer.configure(config, false);

        deserializer = new KafkaAvroDeserializer(schemaRegistry);
        deserializer.configure(config, false);
    }

    @Test
    void Confluent_Kafka_Avro_Serializer로_EventEnvelope_직렬화() {
        // given
        EventEnvelope envelope = createSampleEventEnvelope();

        // when
        byte[] serialized = serializer.serialize(TOPIC, envelope);

        // then
        assertNotNull(serialized);
        assertThat(serialized.length).isGreaterThan(0);
        // Confluent wire format: 첫 바이트는 magic byte (0x00)
        assertThat(serialized[0]).isEqualTo((byte) 0x00);
    }

    @Test
    void Confluent_Kafka_Avro_Deserializer로_EventEnvelope_역직렬화() {
        // given
        EventEnvelope original = createSampleEventEnvelope();
        byte[] serialized = serializer.serialize(TOPIC, original);

        // when
        Object deserialized = deserializer.deserialize(TOPIC, serialized);

        // then
        assertNotNull(deserialized);
        assertThat(deserialized).isInstanceOf(GenericRecord.class);

        GenericRecord record = (GenericRecord) deserialized;
        assertThat(record.get("eventId").toString()).isEqualTo(original.getEventId());
        assertThat(record.get("eventType").toString()).isEqualTo(original.getEventType());
        assertThat(record.get("aggregateId").toString()).isEqualTo(original.getAggregateId());
        assertThat(record.get("aggregateType").toString()).isEqualTo(original.getAggregateType());
        assertThat(record.get("version")).isEqualTo(original.getVersion());
        assertThat(record.get("timestamp")).isEqualTo(original.getTimestamp().toEpochMilli());
    }

    @Test
    void Confluent_직렬화_역직렬화_왕복_테스트() {
        // given
        EventEnvelope original = createSampleEventEnvelope();

        // when
        byte[] serialized = serializer.serialize(TOPIC, original);
        GenericRecord deserialized = (GenericRecord) deserializer.deserialize(TOPIC, serialized);

        // then
        assertThat(deserialized.get("eventId").toString()).isEqualTo(original.getEventId());
        assertThat(deserialized.get("eventType").toString()).isEqualTo(original.getEventType());
        assertThat(deserialized.get("aggregateId").toString()).isEqualTo(original.getAggregateId());
        assertThat(deserialized.get("aggregateType").toString()).isEqualTo(original.getAggregateType());
        assertThat(deserialized.get("version")).isEqualTo(1);
        assertThat(deserialized.get("timestamp")).isEqualTo(original.getTimestamp().toEpochMilli());
        assertThat(deserialized.get("correlationId").toString()).isEqualTo(original.getCorrelationId());
        assertThat(deserialized.get("causationId").toString()).isEqualTo(original.getCausationId());

        // metadata 검증
        @SuppressWarnings("unchecked")
        Map<String, String> metadata = (Map<String, String>) deserialized.get("metadata");
        assertThat(metadata).hasSize(2);
        assertThat(metadata.get("userId").toString()).isEqualTo("user-123");
        assertThat(metadata.get("sourceService").toString()).isEqualTo("test-service");
    }

    @Test
    void 스키마_등록_및_조회_테스트() throws Exception {
        // given
        EventEnvelope envelope = createSampleEventEnvelope();
        Schema schema = envelope.getSchema();

        // when
        int schemaId = schemaRegistry.register(TOPIC + "-value", schema);

        // then
        assertThat(schemaId).isGreaterThan(0);

        // 스키마 조회
        Schema retrievedSchema = schemaRegistry.getById(schemaId);
        assertThat(retrievedSchema.toString()).isEqualTo(schema.toString());
    }

    @Test
    void 스키마_버전_관리_테스트() throws Exception {
        // given
        EventEnvelope envelope = createSampleEventEnvelope();
        Schema schema = envelope.getSchema();
        String subject = TOPIC + "-value";

        // when
        int version1 = schemaRegistry.register(subject, schema);
        int version2 = schemaRegistry.register(subject, schema);

        // then
        assertThat(version1).isEqualTo(version2);
    }

    @Test
    void null_필드_Confluent_직렬화_테스트() {
        // given
        EventEnvelope envelope = EventEnvelope.newBuilder()
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
        byte[] serialized = serializer.serialize(TOPIC, envelope);
        GenericRecord deserialized = (GenericRecord) deserializer.deserialize(TOPIC, serialized);

        // then
        assertThat(deserialized.get("correlationId")).isNull();
        assertThat(deserialized.get("causationId")).isNull();
        assertThat(deserialized.get("metadata")).isNotNull();
        @SuppressWarnings("unchecked")
        Map<String, String> metadata = (Map<String, String>) deserialized.get("metadata");
        assertThat(metadata).isEmpty();
    }

    // Helper method
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
}
