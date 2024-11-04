package com.cuk.catsnap.domain.photographer.repository;

import com.cuk.catsnap.domain.photographer.document.PhotographerReservationLocation;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PhotographerReservationLocationRepository {

    private final MongoOperations mongoOperations;

    public PhotographerReservationLocation save(String reservationLocationContent, Long photographerId) {
        PhotographerReservationLocation photographerReservationLocation = PhotographerReservationLocation.builder()
                .photographerId(photographerId)
                .content(reservationLocationContent)
                .build();
        return mongoOperations.save(photographerReservationLocation);
    }
}
