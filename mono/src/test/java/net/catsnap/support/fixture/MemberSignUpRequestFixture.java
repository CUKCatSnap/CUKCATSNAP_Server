package net.catsnap.support.fixture;

import java.time.LocalDate;
import java.util.List;
import net.catsnap.domain.auth.dto.TermsAgreementRequest;
import net.catsnap.domain.auth.dto.member.request.MemberSignUpRequest;

public class MemberSignUpRequestFixture {

    private String identifier = "test";
    private String password = "password";
    private String nickname = "nickname";
    private LocalDate birthday = LocalDate.now();
    private String phoneNumber = "010-1234-5678";
    private List<TermsAgreementRequest> termsAgreementList = List.of(
        TermsAgreementRequestFixture.termsAgreementRequest().build()
    );

    public static MemberSignUpRequestFixture memberSignUpRequest() {
        return new MemberSignUpRequestFixture();
    }

    public MemberSignUpRequestFixture password(String password) {
        this.password = password;
        return this;
    }

    public MemberSignUpRequest build() {
        return new MemberSignUpRequest(
            this.identifier,
            this.password,
            this.nickname,
            this.birthday,
            this.phoneNumber,
            this.termsAgreementList
        );
    }
}
