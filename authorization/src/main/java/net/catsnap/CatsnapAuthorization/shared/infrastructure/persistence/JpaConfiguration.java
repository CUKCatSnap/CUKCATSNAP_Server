package net.catsnap.CatsnapAuthorization.shared.infrastructure.persistence;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA Infrastructure 설정
 *
 * <p>JPA Auditing 기능을 활성화하여 엔티티의 생성/수정 시간을 자동으로 관리합니다.
 * {@code @CreatedDate}, {@code @LastModifiedDate} 애노테이션이 붙은 필드에
 * 자동으로 타임스탬프가 설정됩니다.</p>
 *
 * <p>이 설정은 여러 애그리거트에서 공유하는 Infrastructure 계층의 책임으로,
 * shared.infrastructure.persistence 패키지에 위치합니다.</p>
 *
 * @see org.springframework.data.annotation.CreatedDate
 * @see org.springframework.data.annotation.LastModifiedDate
 * @see org.springframework.data.jpa.domain.support.AuditingEntityListener
 */
@Configuration
@EnableJpaAuditing
public class JpaConfiguration {

}