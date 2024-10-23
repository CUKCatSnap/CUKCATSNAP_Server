package com.cuk.catsnap.domain.reservation.service;

import com.cuk.catsnap.domain.photographer.entity.Photographer;
import com.cuk.catsnap.domain.reservation.dto.ReservationRequest;
import com.cuk.catsnap.domain.reservation.document.ReservationTimeFormat;
import com.cuk.catsnap.domain.reservation.entity.Program;
import com.cuk.catsnap.domain.reservation.entity.Weekday;

import java.util.List;

public interface ReservationService {

    void createJoinedPhotographerReservationTimeFormat(Photographer photographer);
    String createReservationTimeFormat(ReservationRequest.PhotographerReservationTimeFormat photographerReservationTimeFormat, String reservationTimeFormatId);
    List<ReservationTimeFormat> getMyReservationTimeFormatList();
    void deleteReservationTimeFormat(String reservationTimeFormatId);
    void mappingWeekdayToReservationTimeFormat(String reservationTimeFormatId, Weekday weekday);
    void unmappingWeekdayToReservationTimeFormatByWeekday(Weekday weekday);
    Long createProgram(ReservationRequest.PhotographerProgram photographerProgram, Long programId);
    List<Program> getMyProgramList();
    Long softDeleteProgram(Long programId);
}
