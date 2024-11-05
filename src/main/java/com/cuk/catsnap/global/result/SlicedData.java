package com.cuk.catsnap.global.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlicedData<T> {
    @Schema(description = "스크롤화된 데이터입니다.")
    private T slicedData;
    private Boolean isFirst;
    private Boolean isLast;

    public static <T> SlicedData<T> of(T slicedData, Boolean isFirst, Boolean isLast) {
        return SlicedData.<T>builder()
                .slicedData(slicedData)
                .isFirst(isFirst)
                .isLast(isLast)
                .build();
    }
}
