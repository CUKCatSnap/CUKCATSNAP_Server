package com.cuk.catsnap.global.result;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PagedData<T> {
    private T pagedData;
    private int totalPages;
    private long totalElements;
    private Boolean isFirst;
    private Boolean isLast;
}
