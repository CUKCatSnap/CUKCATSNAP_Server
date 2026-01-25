package net.catsnap.CatsnapReservation.shared.presentation.error;

import static org.assertj.core.api.Assertions.assertThat;

import net.catsnap.CatsnapReservation.shared.ResultCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("PresentationException 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PresentationExceptionTest {

    @Test
    void ResultCode로_예외를_생성한다() {
        // given
        ResultCode resultCode = PresentationErrorCode.INTERNAL_SERVER_ERROR;

        // when
        PresentationException exception = new PresentationException(resultCode);

        // then
        assertThat(exception.getResultCode()).isEqualTo(resultCode);
        assertThat(exception.getMessage()).isEqualTo("서버 내부 오류가 발생했습니다.");
    }

    @Test
    void ResultCode와_원인_예외로_예외를_생성한다() {
        // given
        ResultCode resultCode = PresentationErrorCode.INTERNAL_SERVER_ERROR;
        Throwable cause = new RuntimeException("원인 예외");

        // when
        PresentationException exception = new PresentationException(resultCode, cause);

        // then
        assertThat(exception.getResultCode()).isEqualTo(resultCode);
        assertThat(exception.getMessage()).isEqualTo("서버 내부 오류가 발생했습니다.");
        assertThat(exception.getCause()).isEqualTo(cause);
    }

    @Test
    void ResultCode와_추가_메시지로_예외를_생성한다() {
        // given
        ResultCode resultCode = PresentationErrorCode.INTERNAL_SERVER_ERROR;
        String additionalMessage = "상세 정보";

        // when
        PresentationException exception = new PresentationException(resultCode, additionalMessage);

        // then
        assertThat(exception.getResultCode()).isEqualTo(resultCode);
        assertThat(exception.getMessage()).contains("서버 내부 오류가 발생했습니다.");
        assertThat(exception.getMessage()).contains("상세 정보");
    }

    @Test
    void 인증_에러코드로_예외를_생성한다() {
        // given
        ResultCode resultCode = PresentationErrorCode.UNAUTHORIZED;

        // when
        PresentationException exception = new PresentationException(resultCode);

        // then
        assertThat(exception.getResultCode()).isEqualTo(resultCode);
        assertThat(exception.getMessage()).isEqualTo("인증 정보가 없습니다.");
    }

    @Test
    void 인가_에러코드로_예외를_생성한다() {
        // given
        ResultCode resultCode = PresentationErrorCode.FORBIDDEN;

        // when
        PresentationException exception = new PresentationException(resultCode);

        // then
        assertThat(exception.getResultCode()).isEqualTo(resultCode);
        assertThat(exception.getMessage()).isEqualTo("접근 권한이 없습니다.");
    }
}
