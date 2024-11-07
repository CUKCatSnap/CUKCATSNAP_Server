package com.cuk.catsnap.global.Exception.reservation;

import com.cuk.catsnap.global.Exception.BusinessException;

public class OverLappingTimeException extends BusinessException {

    public OverLappingTimeException(String message) {
        super(message);
    }

    public OverLappingTimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public OverLappingTimeException(Throwable cause) {
        super(cause);
    }
}
