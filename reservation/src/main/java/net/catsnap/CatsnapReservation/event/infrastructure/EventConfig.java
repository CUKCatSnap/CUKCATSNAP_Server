package net.catsnap.CatsnapReservation.event.infrastructure;

import net.catsnap.shared.application.EventDeserializer;
import net.catsnap.shared.infrastructure.AvroEventDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 이벤트 처리 관련 빈 설정
 *
 * <p>Kafka Consumer가 수신한 EventEnvelope의 payload를 도메인 이벤트로 변환할 때 사용됩니다.</p>
 *
 */
@Configuration
public class EventConfig {

    @Bean
    public EventDeserializer eventDeserializer() {
        return new AvroEventDeserializer();
    }
}
