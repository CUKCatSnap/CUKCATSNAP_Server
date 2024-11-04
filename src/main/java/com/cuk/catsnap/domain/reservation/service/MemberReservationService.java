package com.cuk.catsnap.domain.reservation.service;

import com.cuk.catsnap.domain.reservation.dto.ReservationRequest;
import com.cuk.catsnap.domain.reservation.dto.ReservationResponse;
import com.cuk.catsnap.domain.reservation.entity.Reservation;

import java.time.LocalDate;

public interface MemberReservationService {

    Reservation createReservation(ReservationRequest.ReservationBook reservationBook);
    ReservationResponse.PhotographerAvailableReservationTimeList getAvailableReservationTime(LocalDate date, Long photographerId);
}
