package net.catsnap.CatsnapReservation.shared.presentation.error;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("PresentationErrorCode 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PresentationErrorCodeTest {

    @Test
    void 모든_PresentationErrorCode는_EA로_시작하는_코드를_가진다() {
        // when & then
        for (PresentationErrorCode errorCode : PresentationErrorCode.values()) {
            assertThat(errorCode.getCode()).startsWith("EA");
        }
    }

    @Test
    void 모든_PresentationErrorCode는_HttpStatus를_가진다() {
        // when & then
        for (PresentationErrorCode errorCode : PresentationErrorCode.values()) {
            assertThat(errorCode.getHttpStatus()).isNotNull();
        }
    }

    @Test
    void 모든_PresentationErrorCode는_메시지를_가진다() {
        // when & then
        for (PresentationErrorCode errorCode : PresentationErrorCode.values()) {
            assertThat(errorCode.getMessage()).isNotBlank();
        }
    }

    @Test
    void API_요청_에러는_EA0XX_코드를_가진다() {
        // when & then
        assertThat(PresentationErrorCode.NOT_FOUND_API.getCode()).startsWith("EA0");
        assertThat(PresentationErrorCode.MISSING_REQUEST_PARAMETER.getCode()).startsWith("EA0");
        assertThat(PresentationErrorCode.INVALID_REQUEST_BODY.getCode()).startsWith("EA0");
        assertThat(PresentationErrorCode.INTERNAL_SERVER_ERROR.getCode()).startsWith("EA0");
        assertThat(PresentationErrorCode.INVALID_REQUEST_FORMAT.getCode()).startsWith("EA0");
        assertThat(PresentationErrorCode.METHOD_NOT_ALLOWED.getCode()).startsWith("EA0");
        assertThat(PresentationErrorCode.UNSUPPORTED_MEDIA_TYPE.getCode()).startsWith("EA0");
    }

    @Test
    void 인증_인가_에러는_EA1XX_코드를_가진다() {
        // when & then
        assertThat(PresentationErrorCode.UNAUTHORIZED.getCode()).startsWith("EA1");
        assertThat(PresentationErrorCode.INVALID_AUTHORITY.getCode()).startsWith("EA1");
        assertThat(PresentationErrorCode.FORBIDDEN.getCode()).startsWith("EA1");
        assertThat(PresentationErrorCode.INVALID_PASSPORT.getCode()).startsWith("EA1");
        assertThat(PresentationErrorCode.EXPIRED_PASSPORT.getCode()).startsWith("EA1");
    }
}
