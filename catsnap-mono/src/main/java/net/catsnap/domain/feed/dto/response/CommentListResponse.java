package net.catsnap.domain.feed.dto.response;

import java.util.List;

public record CommentListResponse(
    List<CommentResponse> commentResponseList
) {

    public static CommentListResponse from(List<CommentResponse> commentResponseList) {
        return new CommentListResponse(commentResponseList);
    }
}
