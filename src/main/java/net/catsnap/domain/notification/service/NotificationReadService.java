package net.catsnap.domain.notification.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.notification.dto.response.NotificationListResponse;
import net.catsnap.domain.notification.dto.response.NotificationResponse;
import net.catsnap.domain.notification.entity.Notification;
import net.catsnap.domain.notification.repository.NotificationRepository;
import net.catsnap.global.result.SlicedData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationReadService {

    private final NotificationRepository notificationRepository;

    public SlicedData<NotificationListResponse> getRecentNotification(Long userId,
        Pageable pageable, LocalDateTime from) {
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
}
