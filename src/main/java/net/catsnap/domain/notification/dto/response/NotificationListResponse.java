package net.catsnap.domain.notification.dto.response;

import java.util.List;

public record NotificationListResponse(
    List<NotificationResponse> notificationList
) {

    public static NotificationListResponse from(
        List<NotificationResponse> notificationResponseList) {
        return new NotificationListResponse(notificationResponseList);
    }
}
