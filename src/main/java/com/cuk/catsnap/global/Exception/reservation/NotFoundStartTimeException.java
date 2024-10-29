package com.cuk.catsnap.global.Exception.reservation;

import com.cuk.catsnap.global.Exception.BusinessException;

public class NotFoundStartTimeException extends BusinessException {
    public NotFoundStartTimeException(String message) {
        super(message);
    }

    public NotFoundStartTimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundStartTimeException(Throwable cause) {
        super(cause);
    }
}
