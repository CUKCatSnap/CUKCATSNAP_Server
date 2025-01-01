package net.catsnap.global.Exception.reservation;

import net.catsnap.global.Exception.BusinessException;

public class CanNotReserveAfterDeadline extends BusinessException {

    public CanNotReserveAfterDeadline(String message) {
        super(message);
    }

    public CanNotReserveAfterDeadline(String message, Throwable cause) {
        super(message, cause);
    }

    public CanNotReserveAfterDeadline(Throwable cause) {
        super(cause);
    }
}
