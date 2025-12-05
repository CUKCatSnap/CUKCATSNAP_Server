package net.catsnap.support.fixture;

import java.time.LocalDate;
import java.time.LocalDateTime;
import net.catsnap.domain.user.member.entity.Member;
import net.catsnap.domain.user.member.entity.SnsType;

public class MemberFixture {

    private Long id;
    private String identifier = "test";
    private String password = "encodedPassword";
    private String nickname = "nickname";
    private LocalDate birthday = LocalDate.now();
    private String phoneNumber = "010-1234-5678";
    private String profilePhotoUrl = "url";
    private SnsType snstype = SnsType.CATSNAP;
    private String snsId = "snsId";
    private LocalDateTime snsConnectDate = LocalDateTime.now();

    public static MemberFixture member() {
        return new MemberFixture();
    }

    public MemberFixture id(Long id) {
        this.id = id;
        return this;
    }

    public MemberFixture identifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    public Member build() {
        return Member.builder()
            .id(this.id)
            .identifier(this.identifier)
            .password(this.password)
            .nickname(this.nickname)
            .birthday(this.birthday)
            .phoneNumber(this.phoneNumber)
            .profilePhotoUrl(this.profilePhotoUrl)
            .snstype(this.snstype)
            .snsId(this.snsId)
            .snsConnectDate(this.snsConnectDate)
            .build();
    }


}
