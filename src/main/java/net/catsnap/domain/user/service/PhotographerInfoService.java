package net.catsnap.domain.user.service;

import lombok.RequiredArgsConstructor;
import net.catsnap.domain.review.service.ReviewService;
import net.catsnap.domain.user.photographer.dto.response.PhotographerFullyInformationResponse;
import net.catsnap.domain.user.photographer.entity.Photographer;
import net.catsnap.domain.user.photographer.repository.PhotographerRepository;
import net.catsnap.global.Exception.authority.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PhotographerInfoService {

    private final ReviewService reviewService;
    private final PhotographerRepository photographerRepository;

    @Transactional(readOnly = true)
    public PhotographerFullyInformationResponse getPhotographerInfo(Long photographerId) {
        Photographer photographer = photographerRepository.findById(photographerId)
            .orElseThrow(() -> new ResourceNotFoundException("사진작가를 찾을 수 없습니다."));
        Double averageRating = reviewService.getPhotographerRating(photographerId);
        Long reviewCount = reviewService.getRecentReservationCount(photographerId);

        return PhotographerFullyInformationResponse.from(photographer, averageRating, reviewCount);
    }
}
