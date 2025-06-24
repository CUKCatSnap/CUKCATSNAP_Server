package net.catsnap.domain.feed.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FeedCommentLikeCountDto {

    private final Long feedCommentId;
    private final Long likeCount;
}
