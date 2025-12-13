package net.catsnap.CatsnapAuthorization.shared.infrastructure.web.response.code;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("CommonResultCode 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class CommonResultCodeTest {

    @Test
    void 모든_ResultCode는_S로_시작하는_코드를_가진다() {
        // when & then
        for (CommonResultCode resultCode : CommonResultCode.values()) {
            assertThat(resultCode.getCode()).startsWith("S");
        }
    }
}
