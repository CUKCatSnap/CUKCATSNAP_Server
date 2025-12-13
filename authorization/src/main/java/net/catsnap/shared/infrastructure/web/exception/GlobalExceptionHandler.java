package net.catsnap.shared.infrastructure.web.exception;

import lombok.extern.slf4j.Slf4j;
import net.catsnap.shared.infrastructure.web.response.ResultResponse;
import net.catsnap.shared.infrastructure.web.response.errorcode.CommonErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 전역 예외 처리 핸들러
 * <p>
 * 애플리케이션에서 발생하는 예외를 일관된 응답 형식으로 변환합니다.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 존재하지 않는 API 엔드포인트 요청 처리
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ResultResponse<Void>> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.warn("NoHandlerFoundException: {}", e.getMessage());
        return ResultResponse.of(CommonErrorCode.NOT_FOUND_API);
    }

    /**
     * 필수 요청 파라미터 누락 처리
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ResultResponse<Void>> handleMissingServletRequestParameterException(
        MissingServletRequestParameterException e) {
        log.warn("MissingServletRequestParameterException: {}", e.getMessage());
        return ResultResponse.of(CommonErrorCode.MISSING_REQUEST_PARAMETER);
    }

    /**
     * 잘못된 요청 바디 처리
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResultResponse<Void>> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException e) {
        log.warn("HttpMessageNotReadableException: {}", e.getMessage());
        return ResultResponse.of(CommonErrorCode.INVALID_REQUEST_BODY);
    }

    /**
     * 지원하지 않는 HTTP 메서드 처리
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResultResponse<Void>> handleHttpRequestMethodNotSupportedException(
        HttpRequestMethodNotSupportedException e) {
        log.warn("HttpRequestMethodNotSupportedException: {}", e.getMessage());
        return ResultResponse.of(CommonErrorCode.METHOD_NOT_ALLOWED);
    }

    /**
     * 지원하지 않는 미디어 타입 처리
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ResultResponse<Void>> handleHttpMediaTypeNotSupportedException(
        HttpMediaTypeNotSupportedException e) {
        log.warn("HttpMediaTypeNotSupportedException: {}", e.getMessage());
        return ResultResponse.of(CommonErrorCode.UNSUPPORTED_MEDIA_TYPE);
    }

    /**
     * 기타 모든 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultResponse<Void>> handleException(Exception e) {
        log.error("Unexpected exception occurred", e);
        return ResultResponse.of(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }
}
