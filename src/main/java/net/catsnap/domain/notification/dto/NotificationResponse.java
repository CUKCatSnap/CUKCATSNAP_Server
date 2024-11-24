package net.catsnap.domain.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

public class NotificationResponse {

    @Getter
    @Builder
    public static class NotificationList {

        List<Notification> notificationList;
    }

    @Getter
    @Builder
    public static class Notification {

        private Long notificationId;
        private String title;
        private String content;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;

        private Boolean isRead;

        @Schema(description = "알림의 유형입니다. FEED_COMMENT, FEED_LIKE, PHOTOGRAPHER_SUBSCRIBE, PLACE_SUBSCRIBE, RESERVATION")
        private NotificationType notificationType;

        @Schema(description = "해당 알림을 유발한 피드나 리뷰, 예약을 조회하는 API URL입니다.")
        private String relatedURL;
    }
}
