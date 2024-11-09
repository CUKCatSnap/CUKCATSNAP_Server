package com.cuk.catsnap.domain.reservation.service;

import com.cuk.catsnap.domain.reservation.dto.MonthReservationCheckListResponse;
import com.cuk.catsnap.domain.reservation.dto.PhotographerProgramListResponse;
import com.cuk.catsnap.domain.reservation.dto.member.request.MemberReservationRequest;
import com.cuk.catsnap.domain.reservation.dto.member.response.MemberReservationInformationListResponse;
import com.cuk.catsnap.domain.reservation.dto.member.response.PhotographerAvailableReservationTimeListResponse;
import com.cuk.catsnap.domain.reservation.dto.member.response.PhotographerReservationGuidanceResponse;
import com.cuk.catsnap.domain.reservation.entity.Reservation;
import com.cuk.catsnap.domain.reservation.entity.ReservationQueryType;
import com.cuk.catsnap.global.result.SlicedData;
import java.time.LocalDate;
import org.springframework.data.domain.Pageable;

public interface MemberReservationService {

    Reservation createReservation(MemberReservationRequest memberReservationRequest);

    PhotographerAvailableReservationTimeListResponse getAvailableReservationTime(
        LocalDate date, Long photographerId);

    PhotographerProgramListResponse getPhotographerProgram(Long photographerId);

    PhotographerReservationGuidanceResponse getPhotographerReservationGuidance(
        Long photographerId);

    SlicedData<MemberReservationInformationListResponse> getMyReservation(
        ReservationQueryType reservationQueryType,
        Pageable pageable);

    MonthReservationCheckListResponse getReservationListByMonth(LocalDate month);

    MemberReservationInformationListResponse getReservationDetailListByDay(
        LocalDate day);
}
