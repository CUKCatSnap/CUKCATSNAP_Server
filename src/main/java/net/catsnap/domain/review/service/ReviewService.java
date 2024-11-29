package net.catsnap.domain.review.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.member.dto.response.MemberTinyInformationResponse;
import net.catsnap.domain.member.entity.Member;
import net.catsnap.domain.member.repository.MemberRepository;
import net.catsnap.domain.member.service.MemberService;
import net.catsnap.domain.photographer.dto.response.PhotographerTinyInformationResponse;
import net.catsnap.domain.photographer.entity.Photographer;
import net.catsnap.domain.photographer.repository.PhotographerRepository;
import net.catsnap.domain.photographer.service.PhotographerService;
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
import net.catsnap.domain.search.dto.response.ReviewSearchResponse;
import net.catsnap.global.Exception.authority.OwnershipNotFoundException;
import net.catsnap.global.Exception.authority.ResourceNotFoundException;
import net.catsnap.global.aws.s3.ImageClient;
import net.catsnap.global.aws.s3.dto.PresignedUrlResponse;
import net.catsnap.global.security.authority.CatsnapAuthority;
import net.catsnap.global.security.contextholder.GetAuthenticationInfo;
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
    private final MemberService memberService;
    private final PhotographerService photographerService;
    private final ReviewLikeService reviewLikeService;

    @Transactional
    public ReviewPhotoPresignedURLResponse postReview(PostReviewRequest postReviewRequest) {
        Long memberId = GetAuthenticationInfo.getUserId();

        // 예약 정보가 있는지 확인
        Reservation reservation = reservationRepository.findById(
                postReviewRequest.reservationId()
            )
            .orElseThrow(() -> new ResourceNotFoundException("예약 정보를 찾을 수 없습니다."));
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
                        .orElseThrow(() -> new ResourceNotFoundException("리뷰 정보를 찾을 수 없습니다."));
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
                        .orElseThrow(() -> new ResourceNotFoundException("리뷰 정보를 찾을 수 없습니다."));
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

    public ReviewSearchResponse getReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ResourceNotFoundException("리뷰 정보를 찾을 수 없습니다."));
        List<String> photoUrlList = reviewPhotoRepository.findAllByReviewId(reviewId)
            .stream()
            .map(ReviewPhoto::getPhotoFileName)
            .map(imageClient::getDownloadImageUrl)
            .map(URL::toString)
            .toList();

        MemberTinyInformationResponse memberTinyInformation = memberService.getMemberTinyInformation(
            review.getMember().getId());
        PhotographerTinyInformationResponse photographerTinyInformation = photographerService.getPhotographerTinyInformation(
            review.getPhotographer().getId());
        Long likeCount = reviewLikeService.getReviewLikeCount(reviewId);
        Boolean isMeLiked = reviewLikeService.isMeReviewLiked(reviewId);
        return ReviewSearchResponse.of(
            memberTinyInformation, photographerTinyInformation, review, review.getReservation(),
            photoUrlList, likeCount, isMeLiked
        );
    }
}
