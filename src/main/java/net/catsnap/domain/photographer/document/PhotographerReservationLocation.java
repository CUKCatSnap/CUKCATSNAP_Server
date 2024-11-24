package net.catsnap.domain.photographer.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhotographerReservationLocation {

    String id;
    Long photographerId;
    String content;
}
