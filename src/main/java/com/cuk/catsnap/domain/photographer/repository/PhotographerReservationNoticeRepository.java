package com.cuk.catsnap.domain.photographer.repository;

import com.cuk.catsnap.domain.photographer.document.ReservationNotice;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PhotographerReservationNoticeRepository {

    private final MongoOperations mongoOperations;

    public ReservationNotice save(String reservationNoticeContent, Long photographerId) {
        ReservationNotice reservationNotice = ReservationNotice.builder()
                .photographerId(photographerId)
                .content(reservationNoticeContent)
                .build();
        return mongoOperations.save(reservationNotice);
    }
}
