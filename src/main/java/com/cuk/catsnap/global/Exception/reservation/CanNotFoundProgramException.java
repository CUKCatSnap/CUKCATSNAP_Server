package com.cuk.catsnap.global.Exception.reservation;

import com.cuk.catsnap.global.Exception.BusinessException;

public class CanNotFoundProgramException extends BusinessException {
    public CanNotFoundProgramException(String message) {
        super(message);
    }

    public CanNotFoundProgramException(String message, Throwable cause) {
        super(message, cause);
    }

    public CanNotFoundProgramException(Throwable cause) {
        super(cause);
    }
}
