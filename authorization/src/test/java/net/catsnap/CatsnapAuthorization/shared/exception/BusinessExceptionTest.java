package net.catsnap.CatsnapAuthorization.shared.exception;

import static org.assertj.core.api.Assertions.assertThat;

import net.catsnap.CatsnapAuthorization.shared.infrastructure.web.response.ResultCode;
import net.catsnap.CatsnapAuthorization.shared.infrastructure.web.response.errorcode.CommonErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("BusinessException 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class BusinessExceptionTest {

    @Test
    void ResultCode로_예외를_생성한다() {
        // given
        ResultCode resultCode = CommonErrorCode.INTERNAL_SERVER_ERROR;

        // when
        BusinessException exception = new BusinessException(resultCode);

        // then
        assertThat(exception.getResultCode()).isEqualTo(resultCode);
        assertThat(exception.getMessage()).isEqualTo("서버 내부 오류가 발생했습니다.");
    }

    @Test
    void ResultCode와_원인_예외로_예외를_생성한다() {
        // given
        ResultCode resultCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
        Throwable cause = new RuntimeException("원인 예외");

        // when
        BusinessException exception = new BusinessException(resultCode, cause);

        // then
        assertThat(exception.getResultCode()).isEqualTo(resultCode);
        assertThat(exception.getMessage()).isEqualTo("서버 내부 오류가 발생했습니다.");
        assertThat(exception.getCause()).isEqualTo(cause);
    }

    @Test
    void ResultCode와_추가_메시지로_예외를_생성한다() {
        // given
        ResultCode resultCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
        String additionalMessage = "상세 정보";

        // when
        BusinessException exception = new BusinessException(resultCode, additionalMessage);

        // then
        assertThat(exception.getResultCode()).isEqualTo(resultCode);
        assertThat(exception.getMessage()).contains("서버 내부 오류가 발생했습니다.");
        assertThat(exception.getMessage()).contains("상세 정보");
    }
}