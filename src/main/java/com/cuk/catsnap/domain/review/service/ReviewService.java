package com.cuk.catsnap.domain.review.service;

import com.cuk.catsnap.domain.reservation.entity.Reservation;
import com.cuk.catsnap.domain.reservation.repository.ReservationRepository;
import com.cuk.catsnap.domain.review.dto.Response.ReviewPhotoPresignedURLResponse;
import com.cuk.catsnap.domain.review.dto.request.PostReviewRequest;
import com.cuk.catsnap.domain.review.entity.Review;
import com.cuk.catsnap.domain.review.entity.ReviewPhoto;
import com.cuk.catsnap.domain.review.repository.ReviewPhotoRepository;
import com.cuk.catsnap.domain.review.repository.ReviewRepository;
import com.cuk.catsnap.global.Exception.authority.OwnershipNotFoundException;
import com.cuk.catsnap.global.aws.s3.ImageClient;
import com.cuk.catsnap.global.aws.s3.dto.PresignedUrlResponse;
import com.cuk.catsnap.global.security.contextholder.GetAuthenticationInfo;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ImageClient imageClient;
    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewPhotoRepository reviewPhotoRepository;

    @Transactional
    public ReviewPhotoPresignedURLResponse postReview(PostReviewRequest postReviewRequest) {
        Long memberId = GetAuthenticationInfo.getUserId();

        // 예약 정보가 있는지 확인
        Reservation reservation = reservationRepository.findById(
                postReviewRequest.reservationId()
            )
            .orElseThrow(() -> new OwnershipNotFoundException("예약 정보를 찾을 수 없습니다."));
        if (!reservation.getMember().getId().equals(memberId)) {
            throw new OwnershipNotFoundException("예약 정보를 찾을 수 없습니다.");
        }

        Review review = postReviewRequest.toReviewEntity(reservation.getMember(),
            reservation.getPhotographer(),
            reservation);

        reviewRepository.save(review);
        return saveReviewImage(postReviewRequest.photoFileNameList(), review);
    }

    private ReviewPhotoPresignedURLResponse saveReviewImage(List<String> photoFileNameList,
        Review review) {
        List<URL> presignedURL = new ArrayList<>();
        List<URL> photoURL = new ArrayList<>();
        List<ReviewPhoto> reviewPhotoList = new ArrayList<>();

        photoFileNameList.forEach(fileName -> {
            PresignedUrlResponse presignedUrlResponse = imageClient.getUploadImageUrl(fileName);
            reviewPhotoList.add(new ReviewPhoto(
                review,
                presignedUrlResponse.uuidFileName()
            ));
            presignedURL.add(presignedUrlResponse.presignedURL());
            photoURL.add(imageClient.getDownloadImageUrl(presignedUrlResponse.uuidFileName()));
        });
        reviewPhotoRepository.saveAll(reviewPhotoList);

        return ReviewPhotoPresignedURLResponse.of(
            review.getId(),
            presignedURL,
            photoURL
        );
    }
}
