package net.catsnap.CatsnapReservation.shared.domain.error;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("DomainErrorCode 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DomainErrorCodeTest {

    @Test
    void 모든_DomainErrorCode는_ED로_시작하는_코드를_가진다() {
        // when & then
        for (DomainErrorCode errorCode : DomainErrorCode.values()) {
            assertThat(errorCode.getCode()).startsWith("ED");
        }
    }

    @Test
    void 모든_DomainErrorCode는_HttpStatus를_가진다() {
        // when & then
        for (DomainErrorCode errorCode : DomainErrorCode.values()) {
            assertThat(errorCode.getHttpStatus()).isNotNull();
        }
    }

    @Test
    void 모든_DomainErrorCode는_메시지를_가진다() {
        // when & then
        for (DomainErrorCode errorCode : DomainErrorCode.values()) {
            assertThat(errorCode.getMessage()).isNotBlank();
        }
    }
}
