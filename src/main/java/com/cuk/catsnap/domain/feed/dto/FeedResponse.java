package com.cuk.catsnap.domain.feed.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class FeedResponse {

    @Getter
    @Builder
    public static class FeedCommentList {
        List<FeedComment> feedCommentList;
    }

    @Getter
    @Builder
    public static class FeedComment {
        private Long feedCommentId;

        @Schema(description = "해당 댓글이 대댓글이라면 부모의 댓글 id, 아니라면 null")
        private Long parentCommentId;

        private UserInformation writerInformation;
        private String content;
        private Long likeCount;

        @Schema(description = "로그인한 사용자가 해당 댓글에 좋아요를 눌렀는지 여부")
        private Boolean isMeLiked;

        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    public static class UserInformation {
        @Schema(description = "사용자의 id(photographerId  또는 memberId)")
        private Long id;
        @Schema(description = "사용자의 종류를 나타냄 (MEMBER, PHOTOGRAPHER)")
        private UserType userType;
        @Schema(description = "사용자의 프로필 사진 url")
        private String photoUrl;
    }
}
