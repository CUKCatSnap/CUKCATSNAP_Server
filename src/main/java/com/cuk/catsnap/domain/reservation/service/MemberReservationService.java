package com.cuk.catsnap.domain.reservation.service;

import com.cuk.catsnap.domain.reservation.dto.ReservationRequest;
import com.cuk.catsnap.domain.reservation.dto.ReservationResponse;
import com.cuk.catsnap.domain.reservation.entity.Program;
import com.cuk.catsnap.domain.reservation.entity.Reservation;
import com.cuk.catsnap.domain.reservation.entity.ReservationQueryType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;
import java.util.List;

public interface MemberReservationService {

    Reservation createReservation(ReservationRequest.ReservationBook reservationBook);
    ReservationResponse.PhotographerAvailableReservationTimeList getAvailableReservationTime(LocalDate date, Long photographerId);
    List<Program> getPhotographerProgram(Long photographerId);
    ReservationResponse.PhotographerReservationGuidance getPhotographerReservationGuidance(Long photographerId);
    Slice<Reservation> getMyReservation(ReservationQueryType reservationQueryType, Pageable pageable);
    List<Reservation> getReservationListByMonth(LocalDate month);
}
