package net.catsnap.CatsnapReservation.shared.presentation.response;

import static org.assertj.core.api.Assertions.assertThat;

import net.catsnap.CatsnapReservation.shared.ResultCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@DisplayName("ResultResponse 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ResultResponseTest {

    @Test
    void 데이터와_함께_응답을_생성한다() {
        // given
        TestResultCode resultCode = new TestResultCode();
        String testData = "test data";

        // when
        ResponseEntity<ResultResponse<String>> response = ResultResponse.of(resultCode, testData);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("TEST001");
        assertThat(response.getBody().getMessage()).isEqualTo("테스트 성공");
        assertThat(response.getBody().getData()).isEqualTo(testData);
    }

    @Test
    void 데이터없이_응답을_생성한다() {
        // given
        TestResultCode resultCode = new TestResultCode();

        // when
        ResponseEntity<ResultResponse<Void>> response = ResultResponse.of(resultCode);

        // then
        assertThat(response.getStatusCode()).isEqualTo(resultCode.getHttpStatus());
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("TEST001");
        assertThat(response.getBody().getMessage()).isEqualTo("테스트 성공");
        assertThat(response.getBody().getData()).isNull();
    }

    // 테스트용 ResultCode 구현체
    private static class TestResultCode implements ResultCode {

        @Override
        public HttpStatusCode getHttpStatus() {
            return HttpStatus.OK;
        }

        @Override
        public String getCode() {
            return "TEST001";
        }

        @Override
        public String getMessage() {
            return "테스트 성공";
        }
    }
}
