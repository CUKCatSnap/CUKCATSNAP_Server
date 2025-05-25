package net.catsnap.domain.user.photographer.service;

import lombok.RequiredArgsConstructor;
import net.catsnap.domain.user.photographer.entity.Photographer;
import net.catsnap.domain.user.photographer.entity.PhotographerIntroduction;
import net.catsnap.domain.user.photographer.repository.PhotographerIntroductionRepository;
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
}
