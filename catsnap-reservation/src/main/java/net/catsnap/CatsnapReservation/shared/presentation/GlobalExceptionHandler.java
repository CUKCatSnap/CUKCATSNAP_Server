package net.catsnap.CatsnapReservation.shared.presentation;

import lombok.extern.slf4j.Slf4j;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainException;
import net.catsnap.CatsnapReservation.shared.presentation.error.PresentationErrorCode;
import net.catsnap.CatsnapReservation.shared.presentation.error.PresentationException;
import net.catsnap.CatsnapReservation.shared.presentation.response.ResultResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 전역 예외 처리 핸들러
 * <p>
 * 애플리케이션에서 발생하는 예외를 일관된 응답 형식으로 변환합니다.
 * </p>
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 존재하지 않는 API 엔드포인트 요청 처리
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ResultResponse<Void>> handleNoHandlerFoundException(
        NoHandlerFoundException e) {
        log.warn("NoHandlerFoundException: {}", e.getMessage());
        return ResultResponse.of(PresentationErrorCode.NOT_FOUND_API);
    }

    /**
     * 필수 요청 파라미터 누락 처리
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ResultResponse<Void>> handleMissingServletRequestParameterException(
        MissingServletRequestParameterException e) {
        log.warn("MissingServletRequestParameterException: {}", e.getMessage());
        return ResultResponse.of(PresentationErrorCode.MISSING_REQUEST_PARAMETER);
    }

    /**
     * 잘못된 요청 바디 처리
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResultResponse<Void>> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException e) {
        log.warn("HttpMessageNotReadableException: {}", e.getMessage());
        return ResultResponse.of(PresentationErrorCode.INVALID_REQUEST_BODY);
    }

    /**
     * 지원하지 않는 HTTP 메서드 처리
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResultResponse<Void>> handleHttpRequestMethodNotSupportedException(
        HttpRequestMethodNotSupportedException e) {
        log.warn("HttpRequestMethodNotSupportedException: {}", e.getMessage());
        return ResultResponse.of(PresentationErrorCode.METHOD_NOT_ALLOWED);
    }

    /**
     * 지원하지 않는 미디어 타입 처리
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ResultResponse<Void>> handleHttpMediaTypeNotSupportedException(
        HttpMediaTypeNotSupportedException e) {
        log.warn("HttpMediaTypeNotSupportedException: {}", e.getMessage());
        return ResultResponse.of(PresentationErrorCode.UNSUPPORTED_MEDIA_TYPE);
    }

    /**
     * @Valid 검증 실패 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResultResponse<Void>> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String errorMessage =
            fieldError != null ? fieldError.getDefaultMessage() : "입력값이 올바르지 않습니다.";

        log.warn("MethodArgumentNotValidException: {}", errorMessage);
        return ResultResponse.of(PresentationErrorCode.INVALID_REQUEST_BODY);
    }

    /**
     * 프레젠테이션 예외 처리 (인증/인가 실패 포함)
     * <p>
     * 인증 헤더가 없거나 유효하지 않은 경우, 권한이 없는 경우 등
     * 프레젠테이션 레이어에서 발생하는 예외를 처리합니다.
     * </p>
     */
    @ExceptionHandler(PresentationException.class)
    public ResponseEntity<ResultResponse<Void>> handlePresentationException(
        PresentationException e) {
        log.warn("PresentationException: [{}] {}", e.getResultCode().getCode(), e.getMessage());
        return ResultResponse.of(e.getResultCode());
    }

    /**
     * 도메인 예외 처리
     * <p>
     * DomainException에 포함된 ResultCode를 사용하여 응답을 생성합니다.
     * </p>
     */
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ResultResponse<Void>> handleDomainException(DomainException e) {
        log.warn("DomainException: [{}] {}", e.getResultCode().getCode(), e.getMessage());
        return ResultResponse.of(e.getResultCode());
    }

    /**
     * 기타 모든 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultResponse<Void>> handleException(Exception e) {
        log.error("Unexpected exception occurred", e);
        return ResultResponse.of(PresentationErrorCode.INTERNAL_SERVER_ERROR);
    }
}
