package net.catsnap.support.fixture;

import java.time.LocalDate;
import net.catsnap.domain.photographer.entity.Photographer;

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

    public PhotographerFixture id(Long id) {
        this.id = id;
        return this;
    }

    public PhotographerFixture identifier(String identifier) {
        this.identifier = identifier;
        return this;
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
