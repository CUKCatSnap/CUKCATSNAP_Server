package net.catsnap.shared.infrastructure.web.response.errorcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("CommonErrorCode 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class CommonErrorCodeTest {

    @Test
    void 모든_ErrorCode는_E로_시작하는_코드를_가진다() {
        // when & then
        for (CommonErrorCode errorCode : CommonErrorCode.values()) {
            assertThat(errorCode.getCode()).startsWith("E");
        }
    }
}
