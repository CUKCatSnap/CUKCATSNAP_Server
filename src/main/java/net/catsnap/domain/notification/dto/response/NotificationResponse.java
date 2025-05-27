package net.catsnap.domain.notification.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import net.catsnap.domain.notification.entity.NotificationType;

public record NotificationResponse(
    Long notificationId,
    @Schema(description = "가능한 알림 유형: PHOTOGRAPHER_SUBSCRIBE, PLACE_SUBSCRIBE, RESERVATION")
    NotificationType notificationType,
    String title,
    String content,
    @Schema(description = "알림을 유발한 게시글이나 리뷰, 예약의 Id를 의미합니다.")
    Long actorId,
    LocalDateTime readAt
) {

}
