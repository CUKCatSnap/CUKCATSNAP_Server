package net.catsnap.domain.feed.dto.response;

public record FeedCommentResponse(
    Long feedId
) {

    public static FeedCommentResponse of(Long feedId) {
        return new FeedCommentResponse(feedId);
    }
}
