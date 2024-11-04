package com.cuk.catsnap.domain.photographer.repository;

import com.cuk.catsnap.domain.photographer.document.PhotographerReservationNotice;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PhotographerReservationNoticeRepository {

    private final MongoOperations mongoOperations;

    public PhotographerReservationNotice save(String reservationNoticeContent, Long photographerId) {
        PhotographerReservationNotice photographerReservationNotice = PhotographerReservationNotice.builder()
                .photographerId(photographerId)
                .content(reservationNoticeContent)
                .build();
        return mongoOperations.save(photographerReservationNotice);
    }

    public PhotographerReservationNotice findByPhotographerId(Long photographerId) {
        Query query = Query.query(Criteria.where("photographerId").is(photographerId));
        return mongoOperations.findOne(query, PhotographerReservationNotice.class);
    }
}
