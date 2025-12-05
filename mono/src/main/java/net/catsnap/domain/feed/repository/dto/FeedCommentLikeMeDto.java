package net.catsnap.domain.feed.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FeedCommentLikeMeDto {

    private final Long feedCommentId;
    private final Boolean isLiked;
}
