package net.catsnap.domain.notification.repository;

import java.time.LocalDateTime;
import net.catsnap.domain.notification.entity.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Slice<Notification> findByReceiverAndCreatedAtAfter(Long receiverId,
        LocalDateTime createdAt, Pageable pageable);

    Slice<Notification> findByReceiverAndCreatedAtBefore(Long receiverId,
        LocalDateTime createdAt, Pageable pageable);
}
