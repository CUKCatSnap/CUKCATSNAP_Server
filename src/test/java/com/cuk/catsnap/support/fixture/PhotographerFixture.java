package com.cuk.catsnap.support.fixture;

import com.cuk.catsnap.domain.photographer.entity.Photographer;
import java.time.LocalDate;

public class PhotographerFixture {

    private Long id;
    private String identifier = "test";
    private String password = "encodedPassword";
    private String nickname = "nickname";
    private LocalDate birthday = LocalDate.now();
    private String phoneNumber = "010-1234-5678";
    private String profilePhotoUrl = "url";

    public static PhotographerFixture photographer() {
        return new PhotographerFixture();
    }

    public Photographer build() {
        return Photographer.builder()
            .id(this.id)
            .identifier(this.identifier)
            .password(this.password)
            .nickname(this.nickname)
            .birthday(this.birthday)
            .phoneNumber(this.phoneNumber)
            .profilePhotoUrl(this.profilePhotoUrl)
            .build();
    }
}
