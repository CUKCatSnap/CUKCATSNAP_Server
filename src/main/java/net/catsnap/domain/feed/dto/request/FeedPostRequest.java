package net.catsnap.domain.feed.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record FeedPostRequest(
    String title,
    String content,

    @Schema(description = "업로드 하고자 하는 사진 파일의 이름")
    List<String> photoFileNameList) {


}
