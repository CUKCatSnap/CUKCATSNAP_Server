package net.catsnap.CatsnapAuthorization.event.infrastrucutre;

import net.catsnap.shared.application.EventSerializer;
import net.catsnap.shared.infrastructure.AvroEventSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 이벤트 직렬화 설정을 담당하는 구성 클래스
 *
 * <p> 애플리케이션 내에서 사용될 {@link EventSerializer} 구현체를 빈으로 등록합니다.
 * 주로 Outbox 패턴 구현 등에서 이벤트를 바이너리로 직렬화하여 저장소에 저장할 때 사용됩니다.</p>
 *
 * <h3>주요 역할</h3>
 * <ul>
 *   <li><b>EventSerializer 빈 등록</b>: {@link AvroEventSerializer}를 기본 구현체로 제공합니다.</li>
 * </ul>
 *
 * @see EventSerializer
 * @see AvroEventSerializer
 */
@Component
public class EventSerializerConfig {

    /**
     * Avro 기반 이벤트 직렬화 빈을 생성합니다.
     *
     * <p>{@link AvroEventSerializer} 인스턴스를 생성하여 반환합니다.
     * 이 빈은 의존성 주입을 통해 다른 컴포넌트에서 이벤트를 직렬화하는 데 사용됩니다.</p>
     *
     * @return 구성된 {@link EventSerializer} 인스턴스
     */
    @Bean
    public EventSerializer eventSerializer() {
        return new AvroEventSerializer();
    }
}
