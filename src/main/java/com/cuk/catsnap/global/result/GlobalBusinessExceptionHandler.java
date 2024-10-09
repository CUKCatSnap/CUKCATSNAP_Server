package com.cuk.catsnap.global.result;

import com.cuk.catsnap.global.Exception.BusinessException;
import com.cuk.catsnap.global.Exception.member.DuplicatedMemberId;
import com.cuk.catsnap.global.result.errorcode.MemberErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalBusinessExceptionHandler {

    @ExceptionHandler(value = DuplicatedMemberId.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException e){
        ResultCode resultCode = MemberErrorCode.DUPLICATED_SIGNUP_ID;

        return ResponseEntity
                .status(HttpStatus.valueOf(resultCode.getStatus()))
                .body(makeErrorResponse(resultCode));
    }

    private ResultResponse<?> makeErrorResponse(ResultCode resultCode) {
        return ResultResponse.of(resultCode);
    }
}
