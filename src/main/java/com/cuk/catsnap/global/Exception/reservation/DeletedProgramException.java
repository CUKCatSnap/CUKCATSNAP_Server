package com.cuk.catsnap.global.Exception.reservation;

import com.cuk.catsnap.global.Exception.BusinessException;

public class DeletedProgramException extends BusinessException {

    public DeletedProgramException(String message) {
        super(message);
    }

    public DeletedProgramException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeletedProgramException(Throwable cause) {
        super(cause);
    }
}
