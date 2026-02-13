package net.catsnap.shared.infrastructure;

import java.io.IOException;
import net.catsnap.shared.application.EventDeserializer;
import net.catsnap.shared.infrastructure.exception.EventDeserializationException;
import org.apache.avro.AvroRuntimeException;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecordBase;

/**
 * Avro 기반 이벤트 역직렬화 구현체
 *
 * <p>Apache Avro를 사용하여 바이너리 데이터를 이벤트로 역직렬화합니다.
 * 이 클래스는 데이터베이스에서 읽은 payload, 이벤트 리플레이 등 Schema Registry가 필요 없는 상황에서 사용됩니다.</p>
 *
 * <h3>주요 특징</h3>
 * <ul>
 *   <li><b>순수 Avro 역직렬화</b>: Schema Registry 없이 순수 Avro 바이너리를 역직렬화합니다.</li>
 *   <li><b>제네릭 지원</b>: 모든 Avro 생성 이벤트 클래스(SpecificRecordBase)를 처리할 수 있습니다.</li>
 *   <li><b>경량</b>: 스키마 ID 등의 추가 메타데이터 없이 순수 데이터만 역직렬화합니다.</li>
 * </ul>
 *
 * <h3>KafkaValueDeserializer와의 차이</h3>
 * <ul>
 *   <li><b>AvroEventDeserializer</b>: 데이터베이스 등에서 읽을 때 사용. Schema Registry 없이 순수 Avro 바이너리만 처리.</li>
 *   <li><b>KafkaValueDeserializer</b>: Kafka에서 수신할 때 사용. Schema Registry와 통합하여 스키마 ID 포함.</li>
 * </ul>
 *
 * <h3>사용 예시</h3>
 * <pre>{@code
 * // Outbox 패턴에서 데이터베이스에서 읽은 payload 역직렬화
 * byte[] payload = // 데이터베이스에서 읽은 바이트 배열
 *
 * EventDeserializer deserializer = new AvroEventDeserializer();
 * PhotographerCreated event = deserializer.deserialize(payload, PhotographerCreated.class);
 * }</pre>
 *
 * @see EventDeserializer
 */
public class AvroEventDeserializer implements EventDeserializer {

    /**
     * 바이너리 데이터를 이벤트 객체로 역직렬화합니다.
     *
     * <p>내부적으로 Avro를 사용하여 역직렬화하지만, 사용자는 이를 알 필요가 없습니다.</p>
     *
     * @param data        역직렬화할 바이트 배열 (null 불가)
     * @param targetClass 역직렬화할 대상 클래스 (SpecificRecordBase를 상속해야 함)
     * @param <T>         역직렬화 대상 타입
     * @return 역직렬화된 이벤트 객체
     * @throws IllegalArgumentException      data가 null이거나 targetClass가 null인 경우
     * @throws IllegalArgumentException      targetClass가 SpecificRecordBase를 상속하지 않는 경우
     * @throws EventDeserializationException 역직렬화 실패 시
     */
    @Override
    public <T> T deserialize(byte[] data, Class<T> targetClass) {
        if (data == null) {
            throw new IllegalArgumentException("데이터는 null일 수 없습니다.");
        }

        if (targetClass == null) {
            throw new IllegalArgumentException("대상 클래스는 null일 수 없습니다.");
        }

        if (!SpecificRecordBase.class.isAssignableFrom(targetClass)) {
            throw new IllegalArgumentException(
                "지원되지 않는 이벤트 타입입니다: " + targetClass.getName() +
                ". Avro 생성 이벤트 클래스만 역직렬화할 수 있습니다."
            );
        }

        try {
            @SuppressWarnings("unchecked")
            Class<? extends SpecificRecordBase> avroClass = (Class<? extends SpecificRecordBase>) targetClass;
            SpecificDatumReader<? extends SpecificRecordBase> reader = new SpecificDatumReader<>(avroClass);
            BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(data, null);
            @SuppressWarnings("unchecked")
            T result = (T) reader.read(null, decoder); //IOException, AvroRuntimeException 발생 가능
            return result;
        } catch (IOException | AvroRuntimeException e) {
            throw new EventDeserializationException("이벤트 역직렬화에 실패했습니다: " + e.getMessage(), e);
        }
    }
}