package net.catsnap.domain.photographer.converter;

import net.catsnap.domain.photographer.document.PhotographerReservationLocation;
import net.catsnap.domain.photographer.document.PhotographerReservationNotice;
import net.catsnap.domain.photographer.document.PhotographerSetting;
import net.catsnap.domain.photographer.dto.PhotographerResponse;
import org.springframework.stereotype.Component;

@Component
public class PhotographerConverter {

    public PhotographerResponse.PhotographerSetting toPhotographerSetting(
        PhotographerSetting photographerSetting) {
        return PhotographerResponse.PhotographerSetting.builder()
            .autoReservationAccept(photographerSetting.getAutoReservationAccept())
            .enableOverBooking(photographerSetting.getEnableOverBooking())
            .preReservationDays(photographerSetting.getPreReservationDays())
            .build();
    }

    public PhotographerResponse.PhotographerReservationNotice toPhotographerReservationNotice(
        PhotographerReservationNotice photographerReservationNotice) {
        return PhotographerResponse.PhotographerReservationNotice.builder()
            .content(photographerReservationNotice.getContent())
            .build();
    }

    public PhotographerResponse.PhotographerReservationLocation toPhotographerReservationLocation(
        PhotographerReservationLocation photographerReservationLocation) {
        return PhotographerResponse.PhotographerReservationLocation.builder()
            .content(photographerReservationLocation.getContent())
            .build();
    }
}
