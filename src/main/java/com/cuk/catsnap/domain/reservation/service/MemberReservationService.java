package com.cuk.catsnap.domain.reservation.service;

import com.cuk.catsnap.domain.reservation.dto.ReservationRequest;
import com.cuk.catsnap.domain.reservation.entity.Reservation;

public interface MemberReservationService {

    Reservation createReservation(ReservationRequest.ReservationBook reservationBook);
}
