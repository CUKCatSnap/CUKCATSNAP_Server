package net.catsnap.domain.feed.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

public class FeedResponse {

    @Getter
    @Builder
    public static class FeedPhotoPresignedURL {

        @Schema(description = "새로 만들어진 피드의 Id")
        private Long feedId;
        @Schema(description = "사진이 AWS s3 저장될 이름")
        private String photoName;
        @Schema(description = "AWS s3 presignedURL")
        private String presignedURL;
        @Schema(description = "사진 업로드 완료 후, 사진을 접근할 수 있는 URL")
        private String photoURL;
        @Schema(description = "생성 시간")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;
    }
}
