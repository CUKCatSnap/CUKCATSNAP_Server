package net.catsnap.shared.infrastructure.kafka;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import java.util.Map;
import net.catsnap.event.shared.EventEnvelope;
import org.apache.kafka.common.serialization.Deserializer;

/**
 * Kafka Value 역직렬화 구현체
 *
 * <p>Kafka 메시지의 Value를 EventEnvelope로 역직렬화하기 위한 Deserializer입니다.
 * 이 클래스는 Kafka Consumer의 value-deserializer로 사용됩니다.</p>
 *
 * <h3>주요 특징</h3>
 * <ul>
 *   <li><b>Schema Registry 통합</b>: 내부적으로 Confluent의 KafkaAvroDeserializer를 사용하여
 *       스키마를 자동으로 조회하고 버전을 관리합니다.</li>
 *   <li><b>추상화</b>: 외부 모듈은 Avro나 Confluent 구현 세부사항을 알 필요 없이
 *       EventEnvelope만 알면 됩니다.</li>
 *   <li><b>호환성</b>: Confluent Wire Format을 따르므로 스키마 ID가 바이트 배열에 포함되어 있습니다.</li>
 * </ul>
 *
 * <h3>AvroEventDeserializer와의 차이</h3>
 * <ul>
 *   <li><b>KafkaValueDeserializer</b>: Kafka에서 수신할 때 사용. Schema Registry와 통합.</li>
 *   <li><b>AvroEventDeserializer</b>: 데이터베이스 등에서 읽을 때 사용. Schema Registry 없이 순수 Avro 바이너리만 처리.</li>
 * </ul>
 *
 * @see org.apache.kafka.common.serialization.Deserializer
 * @see io.confluent.kafka.serializers.KafkaAvroDeserializer
 */
public class KafkaValueDeserializer implements Deserializer<EventEnvelope> {

    private final KafkaAvroDeserializer avroDeserializer;

    /**
     * 기본 생성자
     *
     * <p>내부적으로 KafkaAvroDeserializer 인스턴스를 생성합니다.</p>
     */
    public KafkaValueDeserializer() {
        this.avroDeserializer = new KafkaAvroDeserializer();
    }

    /**
     * Deserializer를 설정합니다.
     *
     * <p>Kafka Consumer가 이 메서드를 호출하여 설정을 전달합니다.
     * schema.registry.url 등의 설정이 포함되어야 합니다.</p>
     *
     * @param configs Kafka Consumer 설정 (schema.registry.url 포함)
     * @param isKey   키 역직렬화 여부 (Value Deserializer이므로 false)
     */
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        avroDeserializer.configure(configs, isKey);
    }

    /**
     * Kafka 메시지 바이트 배열을 EventEnvelope로 역직렬화합니다.
     *
     * <p>이 메서드는 다음을 수행합니다:</p>
     * <ol>
     *   <li>바이트 배열에서 스키마 ID 추출 (Confluent Wire Format)</li>
     *   <li>Schema Registry에서 스키마 조회</li>
     *   <li>Avro 바이너리 데이터를 EventEnvelope로 역직렬화</li>
     * </ol>
     *
     * @param topic Kafka 토픽 이름
     * @param data  역직렬화할 바이트 배열 (Confluent Wire Format)
     * @return 역직렬화된 EventEnvelope, data가 null인 경우 null 반환
     */
    @Override
    public EventEnvelope deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        return (EventEnvelope) avroDeserializer.deserialize(topic, data);
    }

    /**
     * Deserializer 리소스를 정리합니다.
     *
     * <p>Kafka Consumer가 종료될 때 호출됩니다.</p>
     */
    @Override
    public void close() {
        avroDeserializer.close();
    }
}