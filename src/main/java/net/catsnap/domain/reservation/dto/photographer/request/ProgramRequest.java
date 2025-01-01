package net.catsnap.domain.reservation.dto.photographer.request;

import io.swagger.v3.oas.annotations.media.Schema;
import net.catsnap.domain.photographer.entity.Photographer;
import net.catsnap.domain.reservation.entity.Program;

public record ProgramRequest(
    String title,
    String content,
    Long price,
    @Schema(description = "프로그램의 소요 시간(분)", example = "60", type = "integer")
    Long durationMinutes
) {

    public Program toEntity(Photographer photographer) {
        return Program.builder()
            .photographer(photographer)
            .title(title)
            .content(content)
            .price(price)
            .durationMinutes(durationMinutes)
            .deleted(false)
            .build();
    }
}
