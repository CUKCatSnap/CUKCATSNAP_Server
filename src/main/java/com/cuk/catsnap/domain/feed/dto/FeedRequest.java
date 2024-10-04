package com.cuk.catsnap.domain.feed.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
