package com.cuk.catsnap.global.Exception.reservation;

import com.cuk.catsnap.global.Exception.BusinessException;

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
