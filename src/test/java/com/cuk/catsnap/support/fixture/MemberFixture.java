package com.cuk.catsnap.support.fixture;

import com.cuk.catsnap.domain.member.entity.Member;
import com.cuk.catsnap.domain.member.entity.SnsType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Setter;

@Setter
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
