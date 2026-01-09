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
 * <p>EventSerializer 인터페이스를 구현하며,
 * Apache Avro를 사용하여 이벤트를 바이너리로 직렬화합니다.</p>
 *
 * <p>모든 Avro 생성 이벤트 클래스를 범용적으로 처리할 수 있습니다.</p>
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
