package com.cuk.catsnap.domain.reservation.repository;

import com.cuk.catsnap.domain.reservation.document.ReservationTimeFormat;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class ReservationTimeFormatRepository {

    private final MongoOperations mongoOperations;

    public ReservationTimeFormat save(ReservationTimeFormat reservationTimeFormat) {
        return mongoOperations.save(reservationTimeFormat);
    }

    public UpdateResult update(ReservationTimeFormat reservationTimeFormat) {
        Query query = Query.query(Criteria.where("id")
            .is(reservationTimeFormat.getId())
            .and("photographerId")
            .is(reservationTimeFormat.getPhotographerId()));

        Update update = new Update()
            .set("formatName", reservationTimeFormat.getFormatName())
            .set("startTimeList", reservationTimeFormat.getStartTimeList());

        return mongoOperations.updateFirst(query, update, ReservationTimeFormat.class);
    }

    public List<ReservationTimeFormat> findByPhotographerId(Long photographerId) {
        Query query = Query.query(Criteria.where("photographerId").is(photographerId));
        return mongoOperations.find(query, ReservationTimeFormat.class);
    }

    public DeleteResult deleteById(String reservationTimeFormatId, Long photographerId) {
        Query query = Query.query(Criteria.where("id")
            .is(reservationTimeFormatId)
            .and("photographerId")
            .is(photographerId));
        return mongoOperations.remove(query, ReservationTimeFormat.class);
    }

    public ReservationTimeFormat findById(String reservationTimeFormatId) {
        Query query = Query.query(Criteria.where("id").is(reservationTimeFormatId));
        return mongoOperations.findOne(query, ReservationTimeFormat.class);
    }
}
