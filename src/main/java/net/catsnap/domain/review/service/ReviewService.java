package net.catsnap.domain.review.service;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import net.catsnap.domain.reservation.entity.Reservation;
import net.catsnap.domain.reservation.repository.ReservationRepository;
import net.catsnap.domain.review.dto.Response.ReviewPhotoPresignedURLResponse;
import net.catsnap.domain.review.dto.request.PostReviewRequest;
import net.catsnap.domain.review.entity.Review;
import net.catsnap.domain.review.entity.ReviewLike;
import net.catsnap.domain.review.entity.ReviewPhoto;
import net.catsnap.domain.review.repository.ReviewLikeRepository;
import net.catsnap.domain.review.repository.ReviewPhotoRepository;
import net.catsnap.domain.review.repository.ReviewRepository;
import net.catsnap.domain.search.dto.response.ReviewSearchListResponse;
import net.catsnap.domain.search.dto.response.ReviewSearchResponse;
import net.catsnap.domain.user.member.entity.Member;
import net.catsnap.domain.user.member.repository.MemberRepository;
import net.catsnap.global.Exception.authority.ResourceNotFoundException;
import net.catsnap.global.aws.s3.ImageDownloadClient;
import net.catsnap.global.aws.s3.ImageUploadClient;
import net.catsnap.global.aws.s3.dto.PresignedUrlResponse;
import net.catsnap.global.result.SlicedData;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewPhotoRepository reviewPhotoRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final MemberRepository memberRepository;
    private final ReviewLikeService reviewLikeService;
    private final ImageUploadClient imageUploadClient;
    private final ImageDownloadClient imageDownloadClient;

    public ReviewService(
        ReviewRepository reviewRepository,
        ReservationRepository reservationRepository,
        ReviewPhotoRepository reviewPhotoRepository,
        ReviewLikeRepository reviewLikeRepository,
        MemberRepository memberRepository,
        ReviewLikeService reviewLikeService,
        @Qualifier("reviewImageUploadClient") ImageUploadClient imageUploadClient,
        @Qualifier("reviewImageDownloadClient") ImageDownloadClient imageDownloadClient
    ) {
        this.reviewRepository = reviewRepository;
        this.reservationRepository = reservationRepository;
        this.reviewPhotoRepository = reviewPhotoRepository;
        this.reviewLikeRepository = reviewLikeRepository;
        this.memberRepository = memberRepository;
        this.reviewLikeService = reviewLikeService;
        this.imageUploadClient = imageUploadClient;
        this.imageDownloadClient = imageDownloadClient;
    }

    @Transactional
    public ReviewPhotoPresignedURLResponse postReview(PostReviewRequest postReviewRequest,
        long memberId) {
        // 예약 정보가 있는지 확인
        Reservation reservation = reservationRepository.findById(
                postReviewRequest.reservationId()
            )
            .orElseThrow(() -> new ResourceNotFoundException("예약 정보를 찾을 수 없습니다."));
        reservation.checkMemberOwnership(memberId);
        Review review = postReviewRequest.toReviewEntity(reservation.getMember(),
            reservation.getPhotographer(),
            reservation);

        reviewRepository.save(review);
        return saveReviewImage(postReviewRequest.photoFileNameList(), review);
    }

    @Transactional
    public void toggleReviewLike(Long reviewId, Long userId) {
        reviewLikeRepository.findByReviewIdAndUserId(reviewId, userId)
            .ifPresentOrElse(
                reviewLikeRepository::delete,
                () -> {
                    Member member = memberRepository.getReferenceById(userId);
                    Review review = reviewRepository.findById(reviewId)
                        .orElseThrow(() -> new ResourceNotFoundException("리뷰 정보를 찾을 수 없습니다."));
                    ReviewLike reviewLike = new ReviewLike(
                        review,
                        member
                    );
                    reviewLikeRepository.save(reviewLike);
                }
            );
    }

    private ReviewPhotoPresignedURLResponse saveReviewImage(List<String> photoFileNameList,
        Review review) {
        List<URL> presignedURL = new ArrayList<>();
        List<String> photoURL = new ArrayList<>();
        List<ReviewPhoto> reviewPhotoList = new ArrayList<>();

        photoFileNameList.forEach(fileName -> {
            PresignedUrlResponse presignedUrlResponse = imageUploadClient.getUploadImageUrl(
                fileName);
            reviewPhotoList.add(new ReviewPhoto(
                review,
                presignedUrlResponse.uuidFileName()
            ));
            presignedURL.add(presignedUrlResponse.presignedURL());
            photoURL.add(
                imageDownloadClient.getDownloadImageUrl(presignedUrlResponse.uuidFileName()));
        });
        reviewPhotoRepository.saveAll(reviewPhotoList);

        return ReviewPhotoPresignedURLResponse.of(
            review.getId(),
            presignedURL,
            photoURL
        );
    }

    @Transactional
    public ReviewSearchResponse getReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ResourceNotFoundException("리뷰 정보를 찾을 수 없습니다."));
        List<String> photoUrlList = review.getReivewPhotoFileNameList().stream()
            .map(imageDownloadClient::getDownloadImageUrl)
            .toList();

        Long likeCount = reviewLikeService.getReviewLikeCount(reviewId);
        Boolean isMeLiked = reviewLikeService.isMeReviewLiked(reviewId, userId);
        return ReviewSearchResponse.of(review, photoUrlList, likeCount, isMeLiked);
    }

    @Transactional(readOnly = true)
    public SlicedData<ReviewSearchListResponse> getMyReview(Long memberId, Pageable pageable) {
        Slice<Review> slicedReviewList = reviewRepository.findAllByMemberId(memberId, pageable);
        List<ReviewSearchResponse> reviewSearchResponseList = slicedReviewList.stream()
            .map(review -> {
                List<String> photoUrlList = review.getReivewPhotoFileNameList().stream()
                    .map(imageDownloadClient::getDownloadImageUrl)
                    .toList();
                Long likeCount = reviewLikeService.getReviewLikeCount(review.getId());
                Boolean isMeLiked = reviewLikeService.isMeReviewLiked(review.getId(), memberId);
                return ReviewSearchResponse.of(review, photoUrlList, likeCount, isMeLiked);
            })
            .toList();
        return SlicedData.of(ReviewSearchListResponse.of(reviewSearchResponseList),
            slicedReviewList.isFirst(),
            slicedReviewList.isLast());
    }

    @Transactional(readOnly = true)
    public SlicedData<ReviewSearchListResponse> getPhotographerReview(Long photographerId,
        Long memberId, Pageable pageable) {
        Slice<Review> slicedReviewList = reviewRepository.findAllByReservation_Photographer_Id(
            photographerId, pageable);
        List<ReviewSearchResponse> reviewSearchResponseList = slicedReviewList.stream()
            .map(review -> {
                List<String> photoUrlList = review.getReivewPhotoFileNameList().stream()
                    .map(imageDownloadClient::getDownloadImageUrl)
                    .toList();
                Long likeCount = reviewLikeService.getReviewLikeCount(review.getId());
                Boolean isMeLiked = reviewLikeService.isMeReviewLiked(review.getId(), memberId);
                return ReviewSearchResponse.of(review, photoUrlList, likeCount, isMeLiked);
            })
            .toList();

        return SlicedData.of(ReviewSearchListResponse.of(reviewSearchResponseList),
            slicedReviewList.isFirst(),
            slicedReviewList.isLast());
    }

    @Transactional(readOnly = true)
    public Double getPhotographerRating(Long photographerId) {
        return reviewRepository.findAvgPhotographerScoreByPhotographerId(photographerId);
    }

    @Transactional(readOnly = true)
    public Long getRecentReservationCount(Long photographerId) {
        return reviewRepository.countRecentReviewsByPhotographerId(
            LocalDateTime.now().minusDays(30), photographerId);
    }
}
