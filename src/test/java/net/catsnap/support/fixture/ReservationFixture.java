package net.catsnap.support.fixture;

import net.catsnap.domain.member.entity.Member;
import net.catsnap.domain.photographer.entity.Photographer;
import net.catsnap.domain.reservation.entity.Program;
import net.catsnap.domain.reservation.entity.Reservation;
import net.catsnap.domain.reservation.entity.ReservationState;
import java.time.LocalDateTime;
import org.locationtech.jts.geom.Point;

public class ReservationFixture {

    private Long id;
    private Member member;
    private Photographer photographer = PhotographerFixture.photographer().build();
    private Program program = ProgramFixture.program().build();
    private Point location = PointFixture.point().build();
    private String locationName = "경복궁";
    private LocalDateTime startTime = LocalDateTime.now();
    private LocalDateTime endTime = LocalDateTime.now();
    private ReservationState reservationState = ReservationState.APPROVED;

    public static ReservationFixture reservation() {
        return new ReservationFixture();
    }

    public ReservationFixture id(Long id) {
        this.id = id;
        return this;
    }

    public ReservationFixture member(Member member) {
        this.member = member;
        return this;
    }

    public ReservationFixture photographer(Photographer photographer) {
        this.photographer = photographer;
        return this;
    }

    public ReservationFixture program(Program program) {
        this.program = program;
        return this;
    }

    public ReservationFixture startTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public ReservationFixture endTime(LocalDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public ReservationFixture reservationState(ReservationState reservationState) {
        this.reservationState = reservationState;
        return this;
    }

    public Reservation build() {
        return Reservation.builder()
            .id(this.id)
            .member(this.member)
            .photographer(this.photographer)
            .program(this.program)
            .location(this.location)
            .locationName(this.locationName)
            .startTime(this.startTime)
            .endTime(this.endTime)
            .reservationState(this.reservationState)
            .build();
    }

}
