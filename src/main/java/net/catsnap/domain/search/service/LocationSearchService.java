package net.catsnap.domain.search.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.review.entity.Review;
import net.catsnap.domain.review.repository.ReviewRepository;
import net.catsnap.domain.search.dto.request.LocationSearchRequest;
import net.catsnap.domain.search.dto.response.LocationSearchListResponse;
import net.catsnap.domain.search.dto.response.LocationSearchResponse;
import net.catsnap.domain.user.entity.UserTinyInformation;
import net.catsnap.domain.user.repository.UserTinyInformationRepository;
import net.catsnap.global.Exception.authority.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LocationSearchService {

    private final ReviewRepository reviewRepository;
    private final UserTinyInformationRepository userTinyInformationRepository;

    @Transactional(readOnly = true)
    public LocationSearchListResponse searchLocation(LocationSearchRequest locationSearchRequest) {
        double minX = locationSearchRequest.TopLeftCoordinate().longitude();
        double minY = locationSearchRequest.BottomRightCoordinate().latitude();
        double maxX = locationSearchRequest.BottomRightCoordinate().longitude();
        double maxY = locationSearchRequest.TopLeftCoordinate().latitude();
        List<Review> reviewList = reviewRepository.findAllByLocation(minX, minY, maxX, maxY);

        List<LocationSearchResponse> responseList = new ArrayList<>();

        for (Review review : reviewList) {
            Long photographerId = review.getReservation().getPhotographer().getId();
            UserTinyInformation userInfo = userTinyInformationRepository.findById(photographerId)
                .orElseThrow(() -> new ResourceNotFoundException("작가를 찾을 수 없습니다."));
            LocationSearchResponse response = LocationSearchResponse.from(review, userInfo);
            responseList.add(response);
        }

        return LocationSearchListResponse.from(responseList);
    }
}
