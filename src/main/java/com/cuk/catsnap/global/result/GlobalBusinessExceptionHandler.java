package com.cuk.catsnap.global.result;

import com.cuk.catsnap.global.Exception.BusinessException;
import com.cuk.catsnap.global.result.errorcode.ErrorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalBusinessExceptionHandler {

    private final ErrorRepository errorRepository;

    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<ResultResponse<ResultCode>> handleBusinessException(BusinessException e) {
        ResultCode resultCode = errorRepository.getResultCode(e);

        return ResultResponse.of(resultCode);
    }
}
