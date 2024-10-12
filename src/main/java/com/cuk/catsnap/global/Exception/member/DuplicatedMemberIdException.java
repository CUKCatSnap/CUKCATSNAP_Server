package com.cuk.catsnap.global.Exception.member;

import com.cuk.catsnap.global.Exception.BusinessException;

public class DuplicatedMemberIdException extends BusinessException {

    public DuplicatedMemberIdException(String message) {
        super(message);
    }

    public DuplicatedMemberIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicatedMemberIdException(Throwable cause) {
        super(cause);
    }
}
