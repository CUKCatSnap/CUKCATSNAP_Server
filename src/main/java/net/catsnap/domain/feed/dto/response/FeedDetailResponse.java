package net.catsnap.domain.feed.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import net.catsnap.domain.user.photographer.dto.response.PhotographerTinyInformationResponse;

public record FeedDetailResponse(
    PhotographerTinyInformationResponse photographerInformation,
    Long feedId,

    @Schema(description = "피드의 제목")
    String title,

    @Schema(description = "피드의 내용")
    String content,

    @Schema(description = "피드 사진 URL")
    List<String> photoURLList,

    @Schema(description = "좋아요 수")
    Long likeCount,

    @Schema(description = "내가 좋아요를 눌렀는지")
    Boolean isMeLiked,

    @Schema(description = "피드의 댓글 수")
    Long commentCount,

    @Schema(description = "글 작성 시간")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt,

    CommentListResponse commentListResponse
) {

}
