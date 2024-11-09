package com.cuk.catsnap.domain.reservation.converter;

import com.cuk.catsnap.domain.photographer.entity.Photographer;
import com.cuk.catsnap.domain.reservation.dto.ReservationRequest;
import com.cuk.catsnap.domain.reservation.entity.Program;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationConverter {

    public Program toProgram(ReservationRequest.PhotographerProgram photographerProgram,
        Photographer photographer) {
        return Program.builder()
            .photographer(photographer)
            .title(photographerProgram.getTitle())
            .content(photographerProgram.getContent())
            .price(photographerProgram.getPrice())
            .durationMinutes(photographerProgram.getDurationMinutes())
            .deleted(false)
            .build();
    }
}
