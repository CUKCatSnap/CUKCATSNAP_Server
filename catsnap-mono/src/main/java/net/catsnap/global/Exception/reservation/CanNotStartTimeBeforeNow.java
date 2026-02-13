package net.catsnap.global.Exception.reservation;

import net.catsnap.global.Exception.BusinessException;

public class CanNotStartTimeBeforeNow extends BusinessException {

    public CanNotStartTimeBeforeNow(String message) {
        super(message);
    }

    public CanNotStartTimeBeforeNow(String message, Throwable cause) {
        super(message, cause);
    }

    public CanNotStartTimeBeforeNow(Throwable cause) {
        super(cause);
    }
}
