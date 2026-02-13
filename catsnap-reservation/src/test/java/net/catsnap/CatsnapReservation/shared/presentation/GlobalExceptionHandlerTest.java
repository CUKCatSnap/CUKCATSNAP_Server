package net.catsnap.CatsnapReservation.shared.presentation;

import static org.assertj.core.api.Assertions.assertThat;

import net.catsnap.CatsnapReservation.shared.domain.error.DomainErrorCode;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainException;
import net.catsnap.CatsnapReservation.shared.presentation.error.PresentationErrorCode;
import net.catsnap.CatsnapReservation.shared.presentation.error.PresentationException;
import net.catsnap.CatsnapReservation.shared.presentation.response.ResultResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.NoHandlerFoundException;

@DisplayName("GlobalExceptionHandler 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void NoHandlerFoundException_처리시_NOT_FOUND_API_응답을_반환한다() {
        // given
        NoHandlerFoundException e = new NoHandlerFoundException("GET", "/api/not-found", null);

        // when
        ResponseEntity<ResultResponse<Void>> response = handler.handleNoHandlerFoundException(e);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getCode()).isEqualTo(PresentationErrorCode.NOT_FOUND_API.getCode());
    }

    @Test
    void MissingServletRequestParameterException_처리시_MISSING_REQUEST_PARAMETER_응답을_반환한다() {
        // given
        MissingServletRequestParameterException e =
            new MissingServletRequestParameterException("param", "String");

        // when
        ResponseEntity<ResultResponse<Void>> response =
            handler.handleMissingServletRequestParameterException(e);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getCode())
            .isEqualTo(PresentationErrorCode.MISSING_REQUEST_PARAMETER.getCode());
    }

    @Test
    void HttpMessageNotReadableException_처리시_INVALID_REQUEST_BODY_응답을_반환한다() {
        // given
        HttpMessageNotReadableException e =
            new HttpMessageNotReadableException("Invalid JSON", (org.springframework.http.HttpInputMessage) null);

        // when
        ResponseEntity<ResultResponse<Void>> response =
            handler.handleHttpMessageNotReadableException(e);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getCode())
            .isEqualTo(PresentationErrorCode.INVALID_REQUEST_BODY.getCode());
    }

    @Test
    void HttpRequestMethodNotSupportedException_처리시_METHOD_NOT_ALLOWED_응답을_반환한다() {
        // given
        HttpRequestMethodNotSupportedException e =
            new HttpRequestMethodNotSupportedException("POST");

        // when
        ResponseEntity<ResultResponse<Void>> response =
            handler.handleHttpRequestMethodNotSupportedException(e);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
        assertThat(response.getBody().getCode())
            .isEqualTo(PresentationErrorCode.METHOD_NOT_ALLOWED.getCode());
    }

    @Test
    void HttpMediaTypeNotSupportedException_처리시_UNSUPPORTED_MEDIA_TYPE_응답을_반환한다() {
        // given
        HttpMediaTypeNotSupportedException e =
            new HttpMediaTypeNotSupportedException("Unsupported media type");

        // when
        ResponseEntity<ResultResponse<Void>> response =
            handler.handleHttpMediaTypeNotSupportedException(e);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        assertThat(response.getBody().getCode())
            .isEqualTo(PresentationErrorCode.UNSUPPORTED_MEDIA_TYPE.getCode());
    }

    @Test
    void PresentationException_처리시_해당_ErrorCode_응답을_반환한다() {
        // given
        PresentationException e = new PresentationException(PresentationErrorCode.UNAUTHORIZED);

        // when
        ResponseEntity<ResultResponse<Void>> response = handler.handlePresentationException(e);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody().getCode())
            .isEqualTo(PresentationErrorCode.UNAUTHORIZED.getCode());
    }

    @Test
    void DomainException_처리시_해당_ErrorCode_응답을_반환한다() {
        // given
        DomainException e = new DomainException(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION);

        // when
        ResponseEntity<ResultResponse<Void>> response = handler.handleDomainException(e);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getCode())
            .isEqualTo(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION.getCode());
    }

    @Test
    void 기타_Exception_처리시_INTERNAL_SERVER_ERROR_응답을_반환한다() {
        // given
        Exception e = new RuntimeException("Unexpected error");

        // when
        ResponseEntity<ResultResponse<Void>> response = handler.handleException(e);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().getCode())
            .isEqualTo(PresentationErrorCode.INTERNAL_SERVER_ERROR.getCode());
    }
}
