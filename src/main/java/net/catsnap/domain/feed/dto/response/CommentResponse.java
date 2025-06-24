package net.catsnap.domain.feed.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import net.catsnap.domain.feed.entity.FeedComment;
import net.catsnap.domain.user.dto.response.UserTinyInformationResponse;

public record CommentResponse(
    UserTinyInformationResponse userTinyInformationResponse,
    String content,
    Long feedCommentId,

    @Schema(description = "좋아요 수")
    Long likeCount,

    @Schema(description = "내가 좋아요를 눌렀는지")
    Boolean isMeLiked,

    @Schema(description = "댓글 작성 시간")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt
) {

    public static CommentResponse of(FeedComment feedComment, Long likeCount, Boolean isMeLiked) {
        return new CommentResponse(
            UserTinyInformationResponse.from(feedComment.getUser()),
            feedComment.getContent(),
            feedComment.getId(),
            likeCount,
            isMeLiked,
            feedComment.getCreatedAt()
        );
    }
}
