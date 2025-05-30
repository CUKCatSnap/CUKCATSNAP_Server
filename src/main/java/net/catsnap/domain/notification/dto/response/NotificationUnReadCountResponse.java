package net.catsnap.domain.notification.dto.response;

public record NotificationUnReadCountResponse(
    Long notificationUnReadCount
) {

    public static NotificationUnReadCountResponse of(Long notificationUnReadCount) {
        return new NotificationUnReadCountResponse(notificationUnReadCount);
    }
}
