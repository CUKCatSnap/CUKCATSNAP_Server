package com.cuk.catsnap.global.Exception.member;

import com.cuk.catsnap.global.Exception.BusinessException;
import com.cuk.catsnap.global.result.ResultCode;

public class DuplicatedMemberId extends BusinessException {

    public DuplicatedMemberId(String message) {
        super(message);
    }

    public DuplicatedMemberId(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicatedMemberId(Throwable cause) {
        super(cause);
    }
}
