package net.catsnap.domain.feed.dto.response;

import java.util.List;

public record CommentListResponse(
    List<CommentResponse> commentResponseList
) {

}
