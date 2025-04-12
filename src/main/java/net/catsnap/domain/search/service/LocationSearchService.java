package net.catsnap.domain.search.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.review.entity.Review;
import net.catsnap.domain.review.repository.ReviewRepository;
import net.catsnap.domain.search.dto.request.LocationSearchRequest;
import net.catsnap.domain.search.dto.response.LocationSearchListResponse;
import net.catsnap.domain.search.dto.response.LocationSearchResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LocationSearchService {

    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public LocationSearchListResponse searchLocation(LocationSearchRequest locationSearchRequest) {
        double minX = locationSearchRequest.TopLeftCoordinate().longitude();
        double minY = locationSearchRequest.BottomRightCoordinate().latitude();
        double maxX = locationSearchRequest.BottomRightCoordinate().longitude();
        double maxY = locationSearchRequest.TopLeftCoordinate().latitude();
        List<Review> reviewList = reviewRepository.findAllByLocation(minX, minY, maxX, maxY);
        return LocationSearchListResponse.from(
            reviewList.stream()
                .map(LocationSearchResponse::from)
                .toList()
        );
    }
}
