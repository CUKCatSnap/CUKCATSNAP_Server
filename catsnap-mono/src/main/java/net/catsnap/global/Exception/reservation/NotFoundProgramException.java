package net.catsnap.global.Exception.reservation;

import net.catsnap.global.Exception.BusinessException;

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
