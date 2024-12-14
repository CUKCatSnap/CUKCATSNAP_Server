package net.catsnap.global.result;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import net.catsnap.global.Exception.BusinessException;
import net.catsnap.global.log.service.LogService;
import net.catsnap.global.result.errorcode.CommonErrorCode;
import net.catsnap.global.result.errorcode.ErrorRepository;
import org.slf4j.event.Level;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalBusinessExceptionHandler {

    private final ErrorRepository errorRepository;
    private final LogService logService;

    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<ResultResponse<ResultCode>> handleBusinessException(BusinessException e,
        HttpServletRequest request) throws IOException {
        logService.log(Level.INFO, e, request);
        ResultCode resultCode = errorRepository.getResultCode(e);
        return ResultResponse.of(resultCode);
    }

    @ExceptionHandler(value = NoHandlerFoundException.class)
    public ResponseEntity<ResultResponse<ResultCode>> handleNoHandlerFoundException(
        NoHandlerFoundException e, HttpServletRequest request) throws IOException {
        logService.log(Level.INFO, e, request);
        return ResultResponse.of(CommonErrorCode.NOT_FOUND_API);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity<ResultResponse<ResultCode>> handleMissingServletRequestParameterException(
        MissingServletRequestParameterException e, HttpServletRequest request) throws IOException {
        logService.log(Level.INFO, e, request);
        return ResultResponse.of(CommonErrorCode.MISSING_REQUEST_PARAMETER);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<ResultResponse<ResultCode>> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException e, HttpServletRequest request) throws IOException {
        logService.log(Level.INFO, e, request);
        return ResultResponse.of(CommonErrorCode.INVALID_REQUEST_BODY);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ResultResponse<ResultCode>> handleException(Exception e,
        HttpServletRequest request)
        throws IOException {
        logService.log(Level.ERROR, e, request);
        return ResultResponse.of(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }
}
