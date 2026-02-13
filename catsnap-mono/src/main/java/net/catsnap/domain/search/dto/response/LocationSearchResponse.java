package net.catsnap.domain.search.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import net.catsnap.domain.reservation.dto.ReservationLocation;
import net.catsnap.domain.review.entity.Review;
import net.catsnap.domain.user.entity.UserTinyInformation;
import net.catsnap.domain.user.member.dto.response.MemberTinyInformationResponse;
import net.catsnap.domain.user.photographer.dto.response.PhotographerTinyInformationResponse;

public record LocationSearchResponse(
    MemberTinyInformationResponse memberTinyInformation,
    PhotographerTinyInformationResponse photographerTinyInformation,

    @Schema(description = "리뷰 ID")
    Long reviewId,

    @Schema(description = "글 작성 시간")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt,

    @Schema(description = "예약 시간(사진 촬영 시간)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime reservationDate,

    @Schema(description = "프로그램 이름")
    String programName,

    @Schema(description = "위치 정보")
    ReservationLocation reservationLocation,

    @Schema(description = "리뷰 사진 URL(대표 사진 1장만)")
    String photoUrlList,

    @Schema(description = "작가에 대한 별점")
    Integer photographerScore,

    @Schema(description = "장소에 대한 별점")
    Integer placeScore
) {

    public static LocationSearchResponse from(Review review,
        UserTinyInformation userTinyInformation) {
        return new LocationSearchResponse(
            MemberTinyInformationResponse.from(review.getMember()),
            PhotographerTinyInformationResponse.from(userTinyInformation),
            review.getId(),
            review.getCreatedAt(),
            review.getReservation().getStartTime(),
            review.getReservation().getProgram().getTitle(),
            ReservationLocation.of(review.getReservation()),
            (review.getReviewPhotoList() != null && !review.getReviewPhotoList().isEmpty())
                ? review.getReviewPhotoList().get(0).getPhotoFileName()
                : null,
            review.getPhotographerScore(),
            review.getPlaceScore()
        );
    }
}
