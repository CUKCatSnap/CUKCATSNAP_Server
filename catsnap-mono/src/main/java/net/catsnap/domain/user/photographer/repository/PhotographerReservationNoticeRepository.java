package net.catsnap.domain.user.photographer.repository;

import net.catsnap.domain.user.photographer.document.PhotographerReservationNotice;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PhotographerReservationNoticeRepository {

    private final MongoOperations mongoOperations;

    public PhotographerReservationNotice save(String reservationNoticeContent,
        Long photographerId) {
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

    public UpdateResult updatePhotographerReservationNotice(
        PhotographerReservationNotice photographerReservationNotice) {
        Query query = Query.query(
            Criteria.where("photographerId").is(photographerReservationNotice.getPhotographerId()));
        Update update = new Update()
            .set("content", photographerReservationNotice.getContent());
        return mongoOperations.updateFirst(query, update, PhotographerReservationNotice.class);
    }
}
