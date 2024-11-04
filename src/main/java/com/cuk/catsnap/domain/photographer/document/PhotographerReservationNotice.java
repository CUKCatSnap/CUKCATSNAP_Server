package com.cuk.catsnap.domain.photographer.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhotographerReservationNotice {

    String id;
    Long photographerId;
    String content;
}
