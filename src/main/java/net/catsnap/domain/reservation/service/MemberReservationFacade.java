package net.catsnap.domain.reservation.service;

import lombok.RequiredArgsConstructor;
import net.catsnap.domain.reservation.dto.member.request.MemberReservationRequest;
import net.catsnap.domain.reservation.dto.member.response.ReservationBookResultResponse;
import net.catsnap.domain.reservation.entity.Reservation;
import net.catsnap.domain.reservation.rabbitmq.RabbitmqAddressRequestSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberReservationFacade {

    private final MemberReservationService memberReservationService;
    private final RabbitmqAddressRequestSender addressRequestSender;


    public ReservationBookResultResponse createReservation(
        MemberReservationRequest memberReservationRequest) {

        Reservation reservation = memberReservationService.createReservation(
            memberReservationRequest);
        addressRequestSender.sendRequestAddress(reservation.getId());
        return ReservationBookResultResponse.from(reservation);
    }
}
