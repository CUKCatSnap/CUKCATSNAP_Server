package com.cuk.catsnap.domain.review.service;

import com.cuk.catsnap.domain.member.entity.Member;
import com.cuk.catsnap.domain.member.repository.MemberRepository;
import com.cuk.catsnap.domain.photographer.entity.Photographer;
import com.cuk.catsnap.domain.photographer.repository.PhotographerRepository;
import com.cuk.catsnap.domain.reservation.entity.Reservation;
import com.cuk.catsnap.domain.reservation.repository.ReservationRepository;
import com.cuk.catsnap.domain.review.dto.Response.ReviewPhotoPresignedURLResponse;
import com.cuk.catsnap.domain.review.dto.request.PostReviewRequest;
import com.cuk.catsnap.domain.review.entity.Review;
import com.cuk.catsnap.domain.review.entity.ReviewLike;
import com.cuk.catsnap.domain.review.entity.ReviewPhoto;
import com.cuk.catsnap.domain.review.repository.ReviewLikeRepository;
import com.cuk.catsnap.domain.review.repository.ReviewPhotoRepository;
import com.cuk.catsnap.domain.review.repository.ReviewRepository;
import com.cuk.catsnap.global.Exception.authority.OwnershipNotFoundException;
import com.cuk.catsnap.global.aws.s3.ImageClient;
import com.cuk.catsnap.global.aws.s3.dto.PresignedUrlResponse;
import com.cuk.catsnap.global.security.authority.CatsnapAuthority;
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
    private final ReviewLikeRepository reviewLikeRepository;
    private final MemberRepository memberRepository;
    private final PhotographerRepository photographerRepository;

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

    @Transactional
    public void toggleReviewLike(Long reviewId) {
        CatsnapAuthority authority = GetAuthenticationInfo.getAuthority();
        if (authority.equals(CatsnapAuthority.MEMBER)) {
            memberReviewLike(reviewId);
        } else if (authority.equals(CatsnapAuthority.PHOTOGRAPHER)) {
            photographerReviewLike(reviewId);
        }
    }

    private void memberReviewLike(Long reviewId) {
        Long memberId = GetAuthenticationInfo.getUserId();
        reviewLikeRepository.findByIdAndMemberId(reviewId, memberId)
            .ifPresentOrElse(
                ReviewLike::toggleLike,
                () -> {
                    Member member = memberRepository.getReferenceById(memberId);
                    Review review = reviewRepository.findById(reviewId)
                        .orElseThrow(() -> new OwnershipNotFoundException("리뷰 정보를 찾을 수 없습니다."));
                    ReviewLike reviewLike = new ReviewLike(
                        review,
                        member
                    );
                    reviewLikeRepository.save(reviewLike);
                }
            );
    }

    private void photographerReviewLike(Long reviewId) {
        Long photographerId = GetAuthenticationInfo.getUserId();
        reviewLikeRepository.findByIdAndPhotographerId(reviewId, photographerId)
            .ifPresentOrElse(
                ReviewLike::toggleLike,
                () -> {
                    Photographer photographer = photographerRepository.getReferenceById(
                        photographerId);
                    Review review = reviewRepository.findById(reviewId)
                        .orElseThrow(() -> new OwnershipNotFoundException("리뷰 정보를 찾을 수 없습니다."));
                    ReviewLike reviewLike = new ReviewLike(
                        review,
                        photographer
                    );
                    reviewLikeRepository.save(reviewLike);
                }
            );
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
