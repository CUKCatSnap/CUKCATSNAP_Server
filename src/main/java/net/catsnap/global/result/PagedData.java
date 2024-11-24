package net.catsnap.global.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PagedData<T> {

    @Schema(description = "페이징된 데이터입니다.")
    private T pagedData;
    private int totalPages;
    private long totalElements;
    private Boolean isFirst;
    private Boolean isLast;
}
