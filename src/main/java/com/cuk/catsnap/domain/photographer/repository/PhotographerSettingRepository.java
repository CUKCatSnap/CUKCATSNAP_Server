package com.cuk.catsnap.domain.photographer.repository;

import com.cuk.catsnap.domain.photographer.document.PhotographerSetting;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PhotographerSettingRepository {

    private final MongoOperations mongoOperations;

    public PhotographerSetting save(PhotographerSetting photographerSetting) {
        return mongoOperations.save(photographerSetting);
    }

    public PhotographerSetting findByPhotographerId(Long photographerId) {
        Query query = Query.query(Criteria.where("photographerId").is(photographerId));
        return mongoOperations.findOne(query, PhotographerSetting.class);
    }

    public UpdateResult updatePhotographerSetting(PhotographerSetting photographerSetting) {
        Query query = Query.query(
            Criteria.where("photographerId").is(photographerSetting.getPhotographerId()));
        Update update = new Update()
            .set("autoReservationAccept", photographerSetting.getAutoReservationAccept())
            .set("enableOverBooking", photographerSetting.getEnableOverBooking())
            .set("preReservationDays", photographerSetting.getPreReservationDays());
        return mongoOperations.updateFirst(query, update, PhotographerSetting.class);
    }
}
