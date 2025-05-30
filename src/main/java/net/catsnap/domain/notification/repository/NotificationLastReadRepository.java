package net.catsnap.domain.notification.repository;

import java.util.Optional;
import net.catsnap.domain.notification.entity.NotificationLastRead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationLastReadRepository extends JpaRepository<NotificationLastRead, Long> {

    Optional<NotificationLastRead> findByUserId(Long userId);
}
