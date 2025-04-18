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
import net.catsnap.global.security.contextholder.GetAuthenticationInfo;
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

    public PhotographerSetting getPhotographerSetting() {
        Long photographerId = GetAuthenticationInfo.getUserId();
        return photographerSettingRepository.findByPhotographerId(photographerId);
    }

    public void updatePhotographerSetting(
        PhotographerRequest.PhotographerSetting photographerSetting) {
        Long photographerId = GetAuthenticationInfo.getUserId();
        PhotographerSetting photographerSettingDocument = PhotographerSetting.builder()
            .photographerId(photographerId)
            .autoReservationAccept(photographerSetting.getAutoReservationAccept())
            .enableOverBooking(photographerSetting.getEnableOverBooking())
            .preReservationDays(photographerSetting.getPreReservationDays())
            .build();
        photographerSettingRepository.updatePhotographerSetting(photographerSettingDocument);
    }

    public PhotographerReservationNotice getReservationNotice() {
        return photographerReservationNoticeRepository.findByPhotographerId(
            GetAuthenticationInfo.getUserId());
    }

    public void updateReservationNotice(
        PhotographerRequest.PhotographerReservationNotice photographerReservationNotice) {
        Long photographerId = GetAuthenticationInfo.getUserId();
        PhotographerReservationNotice photographerReservationNoticeDocument = PhotographerReservationNotice.builder()
            .photographerId(photographerId)
            .content(photographerReservationNotice.getContent())
            .build();
        photographerReservationNoticeRepository.updatePhotographerReservationNotice(
            photographerReservationNoticeDocument);
    }

    public PhotographerReservationLocation getReservationLocation() {
        return photographerReservationLocationRepository.findByPhotographerId(
            GetAuthenticationInfo.getUserId());
    }

    public void updateReservationLocation(
        PhotographerRequest.PhotographerReservationLocation photographerReservationLocation) {
        Long photographerId = GetAuthenticationInfo.getUserId();
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
