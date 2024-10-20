package com.cuk.catsnap.domain.reservation.service;

import com.cuk.catsnap.domain.photographer.entity.Photographer;
import com.cuk.catsnap.domain.reservation.dto.ReservationRequest;
import com.cuk.catsnap.domain.reservation.document.ReservationTimeFormat;

import java.util.List;

public interface ReservationService {

    void createJoinedPhotographerReservationTimeFormat(Photographer photographer);
    String createReservationTimeFormat(ReservationRequest.PhotographerReservationTimeFormat photographerReservationTimeFormat, String reservationTimeFormatId);
    List<ReservationTimeFormat> getMyReservationTimeFormatList();
}
