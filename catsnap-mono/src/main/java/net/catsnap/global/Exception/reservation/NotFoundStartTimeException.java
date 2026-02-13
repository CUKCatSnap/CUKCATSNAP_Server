package net.catsnap.global.Exception.reservation;

import net.catsnap.global.Exception.BusinessException;

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
