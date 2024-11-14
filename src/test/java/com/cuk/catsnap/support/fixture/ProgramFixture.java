package com.cuk.catsnap.support.fixture;

import com.cuk.catsnap.domain.photographer.entity.Photographer;
import com.cuk.catsnap.domain.reservation.entity.Program;

public class ProgramFixture {

    private Long id;
    private Photographer photographer = PhotographerFixture.photographer().build();
    private String title = "title";
    private String content = "context";
    private Long price = 50000L;
    private Long durationMinutes = 60L;
    private Boolean deleted = false;

    public static ProgramFixture program() {
        return new ProgramFixture();
    }

    public ProgramFixture photographer(Photographer photographer) {
        this.photographer = photographer;
        return this;
    }

    public ProgramFixture deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public Program build() {
        return Program.builder()
            .id(this.id)
            .photographer(this.photographer)
            .title(this.title)
            .content(this.content)
            .price(this.price)
            .durationMinutes(this.durationMinutes)
            .deleted(this.deleted)
            .build();
    }
}
