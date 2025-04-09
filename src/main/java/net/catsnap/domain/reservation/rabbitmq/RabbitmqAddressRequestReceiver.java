package net.catsnap.domain.reservation.rabbitmq;

import lombok.RequiredArgsConstructor;
import net.catsnap.domain.reservation.repository.ReservationRepository;
import net.catsnap.domain.reservation.service.LocationService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitmqAddressRequestReceiver implements AddressRequestReceiver {

    private final LocationService locationService;
    private final ReservationRepository reservationRepository;

    @Override
    public void receiveAddressRequest(Long reservationId) {

    }
}
