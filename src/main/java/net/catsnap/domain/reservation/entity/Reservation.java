package net.catsnap.domain.reservation.entity;

import net.catsnap.domain.member.entity.Member;
import net.catsnap.domain.notification.entity.ReservationNotification;
import net.catsnap.domain.photographer.entity.Photographer;
import net.catsnap.domain.review.entity.Review;
import net.catsnap.global.entity.BaseTimeEntity;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    //OneToMany

    @OneToMany(mappedBy = "reservation")
    private List<ReservationNotification> reservationNotificationList;

    @OneToOne(mappedBy = "reservation")
    private Review review;

    public void setReservationState(ReservationState reservationState) {
        this.reservationState = reservationState;
    }
}
