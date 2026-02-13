package net.catsnap.domain.user.photographer.service;

import lombok.RequiredArgsConstructor;
import net.catsnap.domain.user.photographer.document.PhotographerReservationLocation;
import net.catsnap.domain.user.photographer.document.PhotographerReservationNotice;
import net.catsnap.domain.user.photographer.document.PhotographerSetting;
import net.catsnap.domain.user.photographer.dto.PhotographerRequest;
import net.catsnap.domain.user.photographer.repository.PhotographerRepository;
import net.catsnap.domain.user.photographer.repository.PhotographerReservationLocationRepository;
import net.catsnap.domain.user.photographer.repository.PhotographerReservationNoticeRepository;
import net.catsnap.domain.user.photographer.repository.PhotographerSettingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PhotographerService {

    private final PhotographerRepository photographerRepository;
    private final PhotographerSettingRepository photographerSettingRepository;
    private final PhotographerReservationNoticeRepository photographerReservationNoticeRepository;
    private final PhotographerReservationLocationRepository photographerReservationLocationRepository;

    public PhotographerSetting getPhotographerSetting(long photographerId) {
        return photographerSettingRepository.findByPhotographerId(photographerId);
    }

    public void updatePhotographerSetting(
        PhotographerRequest.PhotographerSetting photographerSetting, long photographerId) {
        PhotographerSetting photographerSettingDocument = PhotographerSetting.builder()
            .photographerId(photographerId)
            .autoReservationAccept(photographerSetting.getAutoReservationAccept())
            .enableOverBooking(photographerSetting.getEnableOverBooking())
            .preReservationDays(photographerSetting.getPreReservationDays())
            .build();
        photographerSettingRepository.updatePhotographerSetting(photographerSettingDocument);
    }

    public PhotographerReservationNotice getReservationNotice(long photographerId) {
        return photographerReservationNoticeRepository.findByPhotographerId(photographerId);
    }

    public void updateReservationNotice(
        PhotographerRequest.PhotographerReservationNotice photographerReservationNotice,
        long photographerId) {
        PhotographerReservationNotice photographerReservationNoticeDocument = PhotographerReservationNotice.builder()
            .photographerId(photographerId)
            .content(photographerReservationNotice.getContent())
            .build();
        photographerReservationNoticeRepository.updatePhotographerReservationNotice(
            photographerReservationNoticeDocument);
    }

    public PhotographerReservationLocation getReservationLocation(long photographerId) {
        return photographerReservationLocationRepository.findByPhotographerId(photographerId);
    }

    public void updateReservationLocation(
        PhotographerRequest.PhotographerReservationLocation photographerReservationLocation,
        long photographerId) {
        PhotographerReservationLocation photographerReservationLocationDocument = PhotographerReservationLocation.builder()
            .photographerId(photographerId)
            .content(photographerReservationLocation.getContent())
            .build();
        photographerReservationLocationRepository.updatePhotographerReservationLocation(
            photographerReservationLocationDocument);
    }

    public PhotographerSetting findPhotographerSetting(Long photographerId) {
        return photographerSettingRepository.findByPhotographerId(photographerId);
    }
}
