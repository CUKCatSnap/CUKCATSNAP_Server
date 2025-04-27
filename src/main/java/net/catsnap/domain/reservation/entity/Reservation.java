package net.catsnap.domain.reservation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.catsnap.domain.review.entity.Review;
import net.catsnap.domain.user.member.entity.Member;
import net.catsnap.domain.user.photographer.entity.Photographer;
import net.catsnap.global.Exception.authority.OwnershipNotFoundException;
import net.catsnap.global.entity.BaseTimeEntity;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "reservation")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photographer_id")
    private Photographer photographer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id")
    private Program program;

    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point location;

    @Column(name = "location_name")
    private String locationName;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "reservation_state")
    @Enumerated(EnumType.STRING)
    private ReservationState reservationState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_level_id")
    private CityLevel cityLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_level_id")
    private DistrictLevel districtLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "town_level_id")
    private TownLevel townLevel;

    //OneToMany
    @OneToOne(mappedBy = "reservation")
    private Review review;

    public void setReservationState(ReservationState reservationState) {
        this.reservationState = reservationState;
    }

    public void checkPhotographerOwnership(Long photographerId) {
        if (!this.photographer.getId().equals(photographerId)) {
            throw new OwnershipNotFoundException("해당 예약은 작가의 것이 아닙니다.");
        }
    }

    public void checkMemberOwnership(Long memberId) {
        if (!this.member.getId().equals(memberId)) {
            throw new OwnershipNotFoundException("해당 예약은 회원의 것이 아닙니다.");
        }
    }

    public void updateCityLevel(CityLevel cityLevel) {
        this.cityLevel = cityLevel;
    }

    public void updateDistrictLevel(DistrictLevel districtLevel) {
        this.districtLevel = districtLevel;
    }

    public void updateTownLevel(TownLevel townLevel) {
        this.townLevel = townLevel;
    }
}
