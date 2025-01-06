package net.catsnap.global.security.dto;

import java.time.LocalDate;
import java.util.Map;
import net.catsnap.domain.member.entity.SnsType;

public record OAuth2MemberInformation(
    String snsId,
    String nickname,
    LocalDate birthday,
    String phoneNumber,
    String profilePhotoUrl,
    SnsType snsType
) {

    public static OAuth2MemberInformation fromNaver(Map<String, Object> attributes) {
        Map<String, Object> attribute = (Map<String, Object>) attributes.get("response");
        String snsId = (String) attribute.get("id");
        String nickname = (String) attribute.get("nickname");
        String phoneNumber = (String) attribute.get("mobile");
        String profilePhotoUrl = (String) attribute.get("profile_image");
        String birthYear = (String) attribute.get("birthyear");
        String birthMonthDay = (String) attribute.get("birthday");
        LocalDate birthday = LocalDate.of(
            Integer.parseInt(birthYear),
            Integer.parseInt(birthMonthDay.substring(0, 2)),
            Integer.parseInt(birthMonthDay.substring(3))
        );
        return new OAuth2MemberInformation(
            snsId,
            nickname,
            birthday,
            phoneNumber,
            profilePhotoUrl,
            SnsType.NAVER
        );
    }
}
