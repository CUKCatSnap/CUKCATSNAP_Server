package net.catsnap.CatsnapReservation.shared.domain.error;

import static org.assertj.core.api.Assertions.assertThat;

import net.catsnap.CatsnapReservation.shared.ResultCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("DomainException 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DomainExceptionTest {

    @Test
    void ResultCode로_예외를_생성한다() {
        // given
        ResultCode resultCode = DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION;

        // when
        DomainException exception = new DomainException(resultCode);

        // then
        assertThat(exception.getResultCode()).isEqualTo(resultCode);
        assertThat(exception.getMessage()).isEqualTo("해당 값이 유효하지 않습니다.");
    }

    @Test
    void ResultCode와_원인_예외로_예외를_생성한다() {
        // given
        ResultCode resultCode = DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION;
        Throwable cause = new RuntimeException("원인 예외");

        // when
        DomainException exception = new DomainException(resultCode, cause);

        // then
        assertThat(exception.getResultCode()).isEqualTo(resultCode);
        assertThat(exception.getMessage()).isEqualTo("해당 값이 유효하지 않습니다.");
        assertThat(exception.getCause()).isEqualTo(cause);
    }

    @Test
    void ResultCode와_추가_메시지로_예외를_생성한다() {
        // given
        ResultCode resultCode = DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION;
        String additionalMessage = "상세 정보";

        // when
        DomainException exception = new DomainException(resultCode, additionalMessage);

        // then
        assertThat(exception.getResultCode()).isEqualTo(resultCode);
        assertThat(exception.getMessage()).contains("해당 값이 유효하지 않습니다.");
        assertThat(exception.getMessage()).contains("상세 정보");
    }
}
