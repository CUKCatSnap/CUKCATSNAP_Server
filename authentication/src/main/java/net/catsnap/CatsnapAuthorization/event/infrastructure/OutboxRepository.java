package net.catsnap.CatsnapAuthorization.event.infrastructure;

import java.util.List;
import net.catsnap.CatsnapAuthorization.event.domain.Outbox;
import net.catsnap.CatsnapAuthorization.event.domain.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboxRepository extends JpaRepository<Outbox, Long> {

    /**
     * 발행 대기 중인 이벤트 목록을 조회합니다.
     *
     * @param status 조회할 상태
     * @return 발행 대기 중인 Outbox 목록
     */
    List<Outbox> findByStatus(OutboxStatus status);
}