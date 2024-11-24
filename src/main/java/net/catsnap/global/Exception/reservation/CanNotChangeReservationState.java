package net.catsnap.global.Exception.reservation;

import net.catsnap.global.Exception.BusinessException;

public class CanNotChangeReservationState extends BusinessException {

    public CanNotChangeReservationState(String message) {
        super(message);
    }

    public CanNotChangeReservationState(String message, Throwable cause) {
        super(message, cause);
    }

    public CanNotChangeReservationState(Throwable cause) {
        super(cause);
    }
}
