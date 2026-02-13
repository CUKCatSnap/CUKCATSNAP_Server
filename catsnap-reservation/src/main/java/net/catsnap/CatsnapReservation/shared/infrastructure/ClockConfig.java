package net.catsnap.CatsnapReservation.shared.infrastructure;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Clock 설정
 * <p>
 * 시스템 시간 제공을 위한 Infrastructure 계층 설정입니다.
 * DDD 관점에서 Clock은 외부 시스템(OS 시계)에 대한 의존성이므로 Infrastructure로 분류됩니다.
 * <p>
 * Application 계층에서 주입받아 사용하며, 테스트 시 Mock/Fixed Clock으로 교체 가능합니다.
 */
@Configuration
public class ClockConfig {

    /**
     * 시스템 기본 시간대의 Clock 빈 등록
     * <p>
     * 프로덕션: 시스템 시계 사용
     * 테스트: @TestConfiguration으로 Fixed Clock 주입 가능
     *
     * @return 시스템 기본 시간대의 Clock
     */
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
