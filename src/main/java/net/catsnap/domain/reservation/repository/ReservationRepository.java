package net.catsnap.domain.reservation.repository;

import net.catsnap.domain.reservation.entity.Reservation;
import net.catsnap.domain.reservation.entity.ReservationState;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllReservationByPhotographerIdAndStartTimeBetween(Long photographerId,
        LocalDateTime startTime, LocalDateTime endTime);

    @EntityGraph(attributePaths = {"member", "program"})
    List<Reservation> findAllReservationWithEagerByPhotographerIdAndStartTimeBetween(
        Long photographerId, LocalDateTime startTime, LocalDateTime endTime);

    List<Reservation> findAllByPhotographerIdAndStartTimeBetweenOrderByStartTimeAsc(
        Long photographerId, LocalDateTime startTime, LocalDateTime endTime);

    Slice<Reservation> findAllByMemberIdOrderByCreatedAtDesc(Long memberId, Pageable pageable);

    @EntityGraph(attributePaths = {"photographer", "program"})
    Slice<Reservation> findAllByMemberIdAndStartTimeAfterAndReservationStateInOrderByStartTimeAsc(
        Long memberId, LocalDateTime startTime, List<ReservationState> reservationStateList,
        Pageable pageable);

    List<Reservation> findAllReservationByMemberIdAndStartTimeBetween(Long memberId,
        LocalDateTime startTime, LocalDateTime endTime);

    @EntityGraph(attributePaths = {"photographer",
        "program"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Reservation> findAllReservationWithEagerByMemberIdAndStartTimeBetween(Long memberId,
        LocalDateTime startTime, LocalDateTime endTime);
}
