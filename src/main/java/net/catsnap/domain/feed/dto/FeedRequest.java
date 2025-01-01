package net.catsnap.domain.feed.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.catsnap.domain.reservation.dto.ReservationLocation;

public class FeedRequest {

    @Getter
    @NoArgsConstructor
    public static class PostFeedComment {

        @Schema(description = "댓글을 작성할 피드의 id")
        private Long feedId;
        @Schema(description = "작성한 댓글이 대댓글이라면 부모 댓글의 id, 아니라면 null")
        private Long parentCommentId;
        private String comment;
    }

    @Getter
    @NoArgsConstructor
    public static class PostFeed {

        @Schema(description = "작가가 피드 사진을 찍은 위치")
        private ReservationLocation location;
        @Schema(description = "피드 내용")
        private String content;
        @Schema(description = "업로드 하고자 하는 사진 파일의 이름")
        private List<String> photoFileNameList;
    }
}
