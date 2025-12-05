package net.catsnap.domain.feed.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.net.URL;
import java.util.List;

public record FeedPhotoPresignedURLResponse(
    @Schema(description = "작성한 피드의 Id")
    Long feedId,
    @Schema(description = "AWS s3 presignedURL")
    List<URL> presignedURL,
    @Schema(description = "사진 업로드 완료 후, 사진을 접근할 수 있는 URL")
    List<String> photoURL
) {

    public static FeedPhotoPresignedURLResponse of(
        Long feedId,
        List<URL> presignedURL,
        List<String> photoURL
    ) {
        return new FeedPhotoPresignedURLResponse(feedId, presignedURL, photoURL);
    }
}
