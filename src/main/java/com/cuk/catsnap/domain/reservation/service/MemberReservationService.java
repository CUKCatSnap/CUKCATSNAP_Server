package com.cuk.catsnap.domain.reservation.service;

import com.cuk.catsnap.domain.reservation.dto.ReservationResponse;
import com.cuk.catsnap.domain.reservation.dto.member.request.MemberReservationRequest;
import com.cuk.catsnap.domain.reservation.entity.Program;
import com.cuk.catsnap.domain.reservation.entity.Reservation;
import com.cuk.catsnap.domain.reservation.entity.ReservationQueryType;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface MemberReservationService {

    Reservation createReservation(MemberReservationRequest memberReservationRequest);

    ReservationResponse.PhotographerAvailableReservationTimeList getAvailableReservationTime(
        LocalDate date, Long photographerId);

    List<Program> getPhotographerProgram(Long photographerId);

    ReservationResponse.PhotographerReservationGuidance getPhotographerReservationGuidance(
        Long photographerId);

    Slice<Reservation> getMyReservation(ReservationQueryType reservationQueryType,
        Pageable pageable);

    List<Reservation> getReservationListByMonth(LocalDate month);

    ReservationResponse.MemberReservationInformationList getReservationDetailListByDay(
        LocalDate day);
}
