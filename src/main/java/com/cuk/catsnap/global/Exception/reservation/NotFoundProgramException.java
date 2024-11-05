package com.cuk.catsnap.global.Exception.reservation;

import com.cuk.catsnap.global.Exception.BusinessException;

public class NotFoundProgramException extends BusinessException {
    public NotFoundProgramException(String message) {
        super(message);
    }

    public NotFoundProgramException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundProgramException(Throwable cause) {
        super(cause);
    }
}
