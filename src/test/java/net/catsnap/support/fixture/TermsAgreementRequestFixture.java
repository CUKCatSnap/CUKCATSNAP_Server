package net.catsnap.support.fixture;

import net.catsnap.domain.auth.dto.TermsAgreementRequest;

public class TermsAgreementRequestFixture {

    private String termsId = "test";
    private Boolean isAgree = true;

    public static TermsAgreementRequestFixture termsAgreementRequest() {
        return new TermsAgreementRequestFixture();
    }

    public TermsAgreementRequest build() {
        return new TermsAgreementRequest(this.termsId, this.isAgree);
    }
}
