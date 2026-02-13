package net.catsnap.CatsnapReservation.shared.infrastructure;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA Auditing 설정
 * <p>
 * @CreatedDate, @LastModifiedDate 등의 자동 값 주입을 활성화합니다.
 * <p>
 * DDD 관점에서 이는 Infrastructure 계층의 기술적 설정입니다.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}