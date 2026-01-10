package net.catsnap.event.infrastructure;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import net.catsnap.event.application.EventSerializer;
import net.catsnap.event.infrastructure.exception.EventSerializationException;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;

/**
 * Avro 기반 이벤트 직렬화 구현체
 *
 * <p>Apache Avro를 사용하여 이벤트를 순수 바이너리로 직렬화합니다.
 * 이 클래스는 데이터베이스 저장, 파일 저장 등 Schema Registry가 필요 없는 상황에서 사용됩니다.</p>
 *
 * <h3>주요 특징</h3>
 * <ul>
 *   <li><b>순수 Avro 직렬화</b>: Schema Registry 없이 Avro 바이너리만 생성합니다.</li>
 *   <li><b>제네릭 지원</b>: 모든 Avro 생성 이벤트 클래스(SpecificRecordBase)를 처리할 수 있습니다.</li>
 *   <li><b>경량</b>: 스키마 ID 등의 추가 메타데이터 없이 순수 데이터만 직렬화합니다.</li>
 * </ul>
 *
 * <h3>KafkaValueSerializer와의 차이</h3>
 * <ul>
 *   <li><b>AvroEventSerializer</b>: 데이터베이스 등에 저장할 때 사용. Schema Registry 없이 순수 Avro 바이너리만 생성.</li>
 *   <li><b>KafkaValueSerializer</b>: Kafka로 전송할 때 사용. Schema Registry와 통합하여 스키마 ID 포함.</li>
 * </ul>
 *
 * <h3>사용 예시</h3>
 * <pre>{@code
 * // Outbox 패턴에서 데이터베이스에 저장할 payload 생성
 * PhotographerCreated event = PhotographerCreated.newBuilder()
 *     .setPhotographerId("123")
 *     .build();
 *
 * EventSerializer serializer = new AvroEventSerializer();
 * byte[] payload = serializer.serialize(event);
 * // payload를 데이터베이스에 저장
 * }</pre>
 *
 * @see net.catsnap.event.application.EventSerializer
 */
public class AvroEventSerializer implements EventSerializer {

    /**
     * Avro 이벤트를 바이너리로 직렬화합니다.
     *
     * <p>제네릭을 사용하여 모든 Avro 이벤트 타입을 처리합니다. </p>
     *
     * @param event Avro 이벤트 객체
     * @param <T>   SpecificRecordBase를 상속받은 Avro 이벤트 타입
     * @return 직렬화된 바이트 배열
     * @throws EventSerializationException 직렬화 실패 시
     */
    @Override
    public <T extends SpecificRecordBase> byte[] serialize(T event) {
        try {
            SpecificDatumWriter<T> writer = new SpecificDatumWriter<>(event.getSchema());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);
            writer.write(event, encoder);
            encoder.flush();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new EventSerializationException("이벤트 직렬화에 실패했습니다: " + e.getMessage(), e);
        }
    }
}
