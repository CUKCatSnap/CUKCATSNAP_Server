package net.catsnap.CatsnapReservation.shared.presentation.success;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("PresentationSuccessCode 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PresentationSuccessCodeTest {

    @Test
    void 모든_SuccessCode는_S로_시작하는_코드를_가진다() {
        // when & then
        for (PresentationSuccessCode successCode : PresentationSuccessCode.values()) {
            assertThat(successCode.getCode()).startsWith("S");
        }
    }

    @Test
    void 모든_SuccessCode는_HttpStatus를_가진다() {
        // when & then
        for (PresentationSuccessCode successCode : PresentationSuccessCode.values()) {
            assertThat(successCode.getHttpStatus()).isNotNull();
        }
    }

    @Test
    void 모든_SuccessCode는_메시지를_가진다() {
        // when & then
        for (PresentationSuccessCode successCode : PresentationSuccessCode.values()) {
            assertThat(successCode.getMessage()).isNotBlank();
        }
    }
}
