package net.catsnap.domain.search.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import net.catsnap.domain.member.dto.response.MemberTinyInformationResponse;
import net.catsnap.domain.photographer.dto.response.PhotographerTinyInformationResponse;
import net.catsnap.domain.reservation.dto.ReservationLocation;
import net.catsnap.domain.reservation.entity.Reservation;
import net.catsnap.domain.review.entity.Review;

public record ReviewSearchResponse(
    MemberTinyInformationResponse memberTinyInformation,
    PhotographerTinyInformationResponse photographerTinyInformation,

    @Schema(description = "글 작성 시간")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt,

    @Schema(description = "위치 정보")
    ReservationLocation reservationLocation,

    @Schema(description = "리뷰의 내용")
    String content,

    @Schema(description = "리뷰 사진 URL")
    List<String> photoUrlList,

    @Schema(description = "작가에 대한 별점")
    Integer photographerScore,

    @Schema(description = "장소에 대한 별점")
    Integer placeScore,

    @Schema(description = "좋아요 수")
    Long likeCount,

    @Schema(description = "내가 좋아요를 눌렀는지")
    Boolean isMeLiked
) {

    public static ReviewSearchResponse of(
        MemberTinyInformationResponse memberTinyInformation,
        PhotographerTinyInformationResponse photographerTinyInformation,
        Review review,
        Reservation reservation,
        List<String> photoUrlList,
        Long likeCount,
        Boolean isMeLiked
    ) {
        return new ReviewSearchResponse(
            memberTinyInformation,
            photographerTinyInformation,
            review.getCreatedAt(),
            ReservationLocation.of(reservation),
            review.getContent(),
            photoUrlList,
            review.getPhotographerScore(),
            review.getPlaceScore(),
            likeCount,
            isMeLiked
        );
    }

}
