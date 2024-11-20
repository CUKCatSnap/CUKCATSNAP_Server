package com.cuk.catsnap.domain.review.dto.Response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.net.URL;
import java.util.List;

public record ReviewPhotoPresignedURLResponse(
    @Schema(description = "새로 만들어진 리뷰의 Id")
    Long reviewId,
    @Schema(description = "AWS s3 presignedURL")
    List<URL> presignedURL,
    @Schema(description = "사진 업로드 완료 후, 사진을 접근할 수 있는 URL")
    List<URL> photoURL
) {

}
