package net.catsnap.CatsnapAuthorization.event.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 스케줄링 Infrastructure 설정
 *
 * <p>Spring의 스케줄링 기능을 활성화하여 {@code @Scheduled} 애노테이션이 붙은 메서드를
 * 주기적으로 실행할 수 있도록 합니다.</p>
 *
 * <p>글로벌 ErrorHandler를 설정하여 스케줄 작업 실패 시 로그를 기록합니다.</p>
 *
 * @see org.springframework.scheduling.annotation.Scheduled
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {

    private static final Logger log = LoggerFactory.getLogger(SchedulingConfig.class);

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix("scheduled-task-");
        //todo 추후 모니터링 시스템 연동 고려
        scheduler.setErrorHandler(throwable -> log.error("스케줄 작업 실패", throwable));
        return scheduler;
    }
}
