package net.catsnap.event.infrastructure.kafka;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import java.util.Map;
import net.catsnap.event.shared.EventEnvelope;
import org.apache.kafka.common.serialization.Serializer;

/**
 * Kafka Value 직렬화 구현체
 *
 * <p>EventEnvelope을 Kafka 메시지의 Value로 전송하기 위한 Serializer입니다.
 * 이 클래스는 Kafka Producer의 value-serializer로 사용됩니다.</p>
 *
 * <h3>주요 특징</h3>
 * <ul>
 *   <li><b>Schema Registry 통합</b>: 내부적으로 Confluent의 KafkaAvroSerializer를 사용하여
 *       스키마를 자동으로 등록하고 버전을 관리합니다.</li>
 *   <li><b>추상화</b>: 외부 모듈은 Avro나 Confluent 구현 세부사항을 알 필요 없이
 *       EventEnvelope만 알면 됩니다.</li>
 *   <li><b>호환성</b>: Confluent Wire Format을 따르므로 스키마 ID가 바이트 배열에 포함됩니다.</li>
 * </ul>
 *
 * <h3>AvroEventSerializer와의 차이</h3>
 * <ul>
 *   <li><b>KafkaValueSerializer</b>: Kafka로 전송할 때 사용. Schema Registry와 통합.</li>
 *   <li><b>AvroEventSerializer</b>: 데이터베이스 등에 저장할 때 사용. Schema Registry 없이 순수 Avro 바이너리만 생성.</li>
 * </ul>
 *
 * @see org.apache.kafka.common.serialization.Serializer
 * @see io.confluent.kafka.serializers.KafkaAvroSerializer
 */
public class KafkaValueSerializer implements Serializer<EventEnvelope> {

    private final KafkaAvroSerializer avroSerializer;

    /**
     * 기본 생성자
     *
     * <p>내부적으로 KafkaAvroSerializer 인스턴스를 생성합니다.</p>
     */
    public KafkaValueSerializer() {
        this.avroSerializer = new KafkaAvroSerializer();
    }

    /**
     * Serializer를 설정합니다.
     *
     * <p>Kafka Producer가 이 메서드를 호출하여 설정을 전달합니다.
     * schema.registry.url 등의 설정이 포함되어야 합니다.</p>
     *
     * @param configs Kafka Producer 설정 (schema.registry.url 포함)
     * @param isKey   키 직렬화 여부 (Value Serializer이므로 false)
     */
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        avroSerializer.configure(configs, isKey);
    }

    /**
     * EventEnvelope을 Kafka 메시지 바이트 배열로 직렬화합니다.
     *
     * <p>이 메서드는 다음을 수행합니다:</p>
     * <ol>
     *   <li>Schema Registry에 EventEnvelope 스키마를 등록 (최초 1회)</li>
     *   <li>스키마 ID를 조회</li>
     *   <li>Confluent Wire Format으로 바이트 배열 생성:
     *       [Magic Byte(1) | Schema ID(4) | Avro Binary Data(N)]</li>
     * </ol>
     *
     * @param topic Kafka 토픽 이름
     * @param data  직렬화할 EventEnvelope
     * @return Confluent Wire Format의 바이트 배열
     */
    @Override
    public byte[] serialize(String topic, EventEnvelope data) {
        if (data == null) {
            throw new IllegalArgumentException("EventEnvelope 'data' must not be null when serializing for topic '" + topic + "'.");
        }
        return avroSerializer.serialize(topic, data);
    }

    /**
     * Serializer 리소스를 정리합니다.
     *
     * <p>Kafka Producer가 종료될 때 호출됩니다.</p>
     */
    @Override
    public void close() {
        avroSerializer.close();
    }
}