package net.catsnap.CatsnapAuthorization.event.infrastructure;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 스케줄링 Infrastructure 설정
 *
 * <p>Spring의 스케줄링 기능을 활성화하여 {@code @Scheduled} 애노테이션이 붙은 메서드를
 * 주기적으로 실행할 수 있도록 합니다.</p>
 *
 * @see org.springframework.scheduling.annotation.Scheduled
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {

}
