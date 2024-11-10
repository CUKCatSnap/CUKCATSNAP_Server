package com.cuk.catsnap.domain.photographer.converter;

import com.cuk.catsnap.domain.photographer.document.PhotographerReservationLocation;
import com.cuk.catsnap.domain.photographer.document.PhotographerReservationNotice;
import com.cuk.catsnap.domain.photographer.document.PhotographerSetting;
import com.cuk.catsnap.domain.photographer.dto.PhotographerRequest;
import com.cuk.catsnap.domain.photographer.dto.PhotographerResponse;
import com.cuk.catsnap.domain.photographer.entity.Photographer;
import org.springframework.stereotype.Component;

@Component
public class PhotographerConverter {

    public Photographer photographerSignUpToPhotographer(
        PhotographerRequest.PhotographerSignUp photographerSignUp, String encodedPassword) {
        return Photographer.builder()
            .identifier(photographerSignUp.getIdentifier())
            .password(encodedPassword)
            .birthday(photographerSignUp.getBirthday())
            .nickname(photographerSignUp.getNickname())
            .phoneNumber(photographerSignUp.getPhoneNumber())
            .build();
    }

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
