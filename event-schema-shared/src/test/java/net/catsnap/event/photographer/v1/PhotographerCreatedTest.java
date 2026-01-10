package net.catsnap.event.photographer.v1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import java.util.HashMap;
import java.util.Map;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

/**
 * PhotographerCreated 이벤트의 Confluent Schema Registry 호환성 테스트
 * - Kafka Avro Serializer/Deserializer 사용
 * - Mock Schema Registry를 사용한 스키마 등록 및 호환성 검증
 */
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PhotographerCreatedTest {

    private static final String TOPIC = "photographer-events";
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

    @AfterEach  
    void tearDown() {  
        if (serializer != null) {  
            serializer.close();  
        }  
        if (deserializer != null) {  
            deserializer.close();  
        }  
    }  

    @Test
    void PhotographerCreated_기본_생성_및_필드_검증() {
        // given
        Long photographerId = 12345L;

        // when
        PhotographerCreated event = PhotographerCreated.newBuilder()
                .setPhotographerId(photographerId)
                .build();

        // then
        assertThat(event.getPhotographerId()).isEqualTo(photographerId);
    }

    @Test
    void Confluent_Kafka_Avro_Serializer로_PhotographerCreated_직렬화() {
        // given
        PhotographerCreated event = createSamplePhotographerCreated();

        // when
        byte[] serialized = serializer.serialize(TOPIC, event);

        // then
        assertNotNull(serialized);
        assertThat(serialized.length).isGreaterThan(0);
        // Confluent wire format: 첫 바이트는 magic byte (0x00)
        assertThat(serialized[0]).isEqualTo((byte) 0x00);
    }

    @Test
    void Confluent_Kafka_Avro_Deserializer로_PhotographerCreated_역직렬화() {
        // given
        PhotographerCreated original = createSamplePhotographerCreated();
        byte[] serialized = serializer.serialize(TOPIC, original);

        // when
        Object deserialized = deserializer.deserialize(TOPIC, serialized);

        // then
        assertNotNull(deserialized);
        assertThat(deserialized).isInstanceOf(GenericRecord.class);

        GenericRecord record = (GenericRecord) deserialized;
        assertThat(record.get("photographerId")).isEqualTo(original.getPhotographerId());
    }

    @Test
    void Confluent_직렬화_역직렬화_왕복_테스트() {
        // given
        PhotographerCreated original = createSamplePhotographerCreated();

        // when
        byte[] serialized = serializer.serialize(TOPIC, original);
        GenericRecord deserialized = (GenericRecord) deserializer.deserialize(TOPIC, serialized);

        // then
        assertThat(deserialized.get("photographerId")).isEqualTo(original.getPhotographerId());
    }

    @Test
    void 스키마_등록_및_조회_테스트() throws Exception {
        // given
        PhotographerCreated event = createSamplePhotographerCreated();
        Schema schema = event.getSchema();

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
        PhotographerCreated event = createSamplePhotographerCreated();
        Schema schema = event.getSchema();
        String subject = TOPIC + "-value";

        // when
        int version1 = schemaRegistry.register(subject, schema);
        int version2 = schemaRegistry.register(subject, schema);

        // then
        assertThat(version1).isEqualTo(version2);
    }

    @Test
    void PhotographerCreated_스키마_호환성_검증() {
        // given
        PhotographerCreated event = createSamplePhotographerCreated();

        // when & then
        assertNotNull(event.getSchema());
        assertThat(event.getSchema().getName()).isEqualTo("PhotographerCreated");
        assertThat(event.getSchema().getNamespace()).isEqualTo("net.catsnap.event.photographer.v1");
        assertThat(event.getSchema().getFields()).hasSize(1);
        assertThat(event.getSchema().getField("photographerId")).isNotNull();
    }

    @Test
    void 다양한_photographerId_값_직렬화_테스트() {
        // given
        Long[] testIds = {1L, 100L, 999999L, Long.MAX_VALUE};

        for (Long testId : testIds) {
            // when
            PhotographerCreated event = PhotographerCreated.newBuilder()
                    .setPhotographerId(testId)
                    .build();
            byte[] serialized = serializer.serialize(TOPIC, event);
            GenericRecord deserialized = (GenericRecord) deserializer.deserialize(TOPIC, serialized);

            // then
            assertThat(deserialized.get("photographerId")).isEqualTo(testId);
        }
    }

    // Helper method
    private PhotographerCreated createSamplePhotographerCreated() {
        return PhotographerCreated.newBuilder()
                .setPhotographerId(12345L)
                .build();
    }
}