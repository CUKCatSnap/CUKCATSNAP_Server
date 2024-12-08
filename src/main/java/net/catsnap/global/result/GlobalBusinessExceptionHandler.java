package net.catsnap.global.result;

import lombok.RequiredArgsConstructor;
import net.catsnap.global.Exception.BusinessException;
import net.catsnap.global.result.errorcode.CommonErrorCode;
import net.catsnap.global.result.errorcode.ErrorRepository;
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

    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<ResultResponse<ResultCode>> handleBusinessException(BusinessException e) {
        ResultCode resultCode = errorRepository.getResultCode(e);

        return ResultResponse.of(resultCode);
    }

    @ExceptionHandler(value = NoHandlerFoundException.class)
    public ResponseEntity<ResultResponse<ResultCode>> handleNoHandlerFoundException(
        NoHandlerFoundException e) {
        return ResultResponse.of(CommonErrorCode.NOT_FOUND_API);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity<ResultResponse<ResultCode>> handleMissingServletRequestParameterException(
        MissingServletRequestParameterException e) {
        return ResultResponse.of(CommonErrorCode.MISSING_REQUEST_PARAMETER);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<ResultResponse<ResultCode>> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException e) {
        return ResultResponse.of(CommonErrorCode.INVALID_REQUEST_BODY);
    }
}
