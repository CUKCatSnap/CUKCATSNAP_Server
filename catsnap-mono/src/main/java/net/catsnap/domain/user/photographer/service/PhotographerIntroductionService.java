package net.catsnap.domain.user.photographer.service;

import lombok.RequiredArgsConstructor;
import net.catsnap.domain.user.photographer.dto.request.PhotographerIntroductionResponse;
import net.catsnap.domain.user.photographer.entity.Photographer;
import net.catsnap.domain.user.photographer.entity.PhotographerIntroduction;
import net.catsnap.domain.user.photographer.repository.PhotographerIntroductionRepository;
import net.catsnap.global.Exception.authority.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PhotographerIntroductionService {

    private final PhotographerIntroductionRepository photographerIntroductionRepository;

    @Transactional
    public void initPhotographerIntroduction(Photographer photographer) {
        PhotographerIntroduction photographerIntroduction = PhotographerIntroduction.builder()
            .photographer(photographer)
            .content("")
            .build();

        photographerIntroductionRepository.save(photographerIntroduction);
    }

    @Transactional
    public void updatePhotographerIntroduction(Long photographerId, String introduction) {

        PhotographerIntroduction photographerIntroduction = photographerIntroductionRepository.findByPhotographerId(
                photographerId)
            .orElseThrow(() -> new ResourceNotFoundException("해당 사진작가의 자기소개가 존재하지 않습니다."));

        photographerIntroduction.updateIntroduction(introduction);
        photographerIntroductionRepository.save(photographerIntroduction);
    }

    @Transactional(readOnly = true)
    public PhotographerIntroductionResponse getPhotographerIntroduction(Long photographerId) {
        PhotographerIntroduction photographerIntroduction = photographerIntroductionRepository.findByPhotographerId(
                photographerId)
            .orElseThrow(() -> new ResourceNotFoundException("해당 사진작가의 자기소개가 존재하지 않습니다."));

        return PhotographerIntroductionResponse.from(photographerIntroduction);
    }
}
