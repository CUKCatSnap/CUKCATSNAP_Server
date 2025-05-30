package net.catsnap.domain.notification.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.notification.dto.response.NotificationListResponse;
import net.catsnap.domain.notification.dto.response.NotificationResponse;
import net.catsnap.domain.notification.entity.Notification;
import net.catsnap.domain.notification.entity.NotificationLastRead;
import net.catsnap.domain.notification.repository.NotificationLastReadRepository;
import net.catsnap.domain.notification.repository.NotificationRepository;
import net.catsnap.global.Exception.authority.OwnershipNotFoundException;
import net.catsnap.global.result.SlicedData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationReadService {

    private final NotificationRepository notificationRepository;
    private final NotificationLastReadRepository notificationLastReadRepository;

    public SlicedData<NotificationListResponse> getRecentNotification(Long userId,
        Pageable pageable, LocalDateTime from) {
        readNotification(userId);
        Slice<Notification> notificationSlice = notificationRepository.findByReceiverAndCreatedAtAfter(
            userId, from, pageable);
        List<NotificationResponse> notificationResponseList = notificationSlice.getContent()
            .stream()
            .map(NotificationResponse::from)
            .toList();
        return SlicedData.of(NotificationListResponse.from(notificationResponseList),
            notificationSlice.isFirst(),
            notificationSlice.isLast());
    }

    public SlicedData<NotificationListResponse> getOldNotification(Long userId,
        Pageable pageable, LocalDateTime to) {
        Slice<Notification> notificationSlice = notificationRepository.findByReceiverAndCreatedAtBefore(
            userId, to, pageable);
        List<NotificationResponse> notificationResponseList = notificationSlice.getContent()
            .stream()
            .map(NotificationResponse::from)
            .toList();
        return SlicedData.of(NotificationListResponse.from(notificationResponseList),
            notificationSlice.isFirst(),
            notificationSlice.isLast());
    }

    private void readNotification(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastReadAt = notificationLastReadRepository.findByUserId(userId)
            .map(NotificationLastRead::getLastReadAt)
            .orElse(now);
        notificationRepository.updateReadAtAfter(now, userId, lastReadAt);
        updateReadNotificationTime(userId, now);
    }

    private void updateReadNotificationTime(Long userId, LocalDateTime now) {
        notificationLastReadRepository.findByUserId(userId)
            .ifPresentOrElse(
                notificationLastRead -> {
                    notificationLastRead.updateLastReadAt(now);
                    notificationLastReadRepository.save(notificationLastRead);
                },
                () -> {
                    throw new OwnershipNotFoundException("해당 유저의 알림 읽음 시간이 없습니다.");
                }
            );
    }
}
