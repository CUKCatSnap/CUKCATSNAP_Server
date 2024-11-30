package net.catsnap.support.fixture;

import java.time.LocalDate;
import java.util.List;
import net.catsnap.domain.auth.dto.TermsAgreementRequest;
import net.catsnap.domain.auth.dto.photographer.request.PhotographerSignUpRequest;

public class PhotographerSignUpRequestFixture {

    private String identifier = "test";
    private String password = "password";
    private String nickname = "nickname";
    private LocalDate birthday = LocalDate.now();
    private String phoneNumber = "010-1234-5678";
    private List<TermsAgreementRequest> termsAgreementList = List.of(
        TermsAgreementRequestFixture.termsAgreementRequest().build()
    );

    public static PhotographerSignUpRequestFixture photographerSignUpRequest() {
        return new PhotographerSignUpRequestFixture();
    }

    public PhotographerSignUpRequestFixture password(String password) {
        this.password = password;
        return this;
    }

    public PhotographerSignUpRequest build() {
        return new PhotographerSignUpRequest(
            this.identifier,
            this.password,
            this.nickname,
            this.birthday,
            this.phoneNumber,
            this.termsAgreementList
        );
    }
}
