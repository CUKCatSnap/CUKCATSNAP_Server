package net.catsnap.domain.search.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import net.catsnap.domain.photographer.dto.response.PhotographerFullyInformationResponse;
import net.catsnap.domain.reservation.dto.ReservationResponse;

public record ReviewSearchResponse(
    PhotographerFullyInformationResponse photographerFullyInformation,

    @Schema(description = "글 작성 시간")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt,

    @Schema(description = "위치 정보")
    ReservationResponse.Location location,

    @Schema(description = "리뷰의 내용")
    String content,

    @Schema(description = "리뷰 사진 URL")
    List<URL> photoUrlList,

    @Schema(description = "작가에 대한 별점")
    Double photographerRating,

    @Schema(description = "장소에 대한 별점")
    Double placeRating,

    @Schema(description = "좋아요 수")
    Long likeCount,

    @Schema(description = "내가 좋아요를 눌렀는지")
    Boolean isMeLiked
) {

}
