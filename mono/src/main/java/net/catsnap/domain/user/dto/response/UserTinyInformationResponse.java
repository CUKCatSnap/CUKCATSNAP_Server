package net.catsnap.domain.user.dto.response;


import net.catsnap.domain.user.entity.User;
import net.catsnap.domain.user.entity.UserType;

public record UserTinyInformationResponse(
    Long userId,
    String nickname,
    String profilePhotoUrl,
    UserType userType
) {

    public static UserTinyInformationResponse from(User user) {
        return new UserTinyInformationResponse(
            user.getId(),
            user.getNickname(),
            user.getProfilePhotoUrl(),
            user.getUserType()
        );
    }
}
