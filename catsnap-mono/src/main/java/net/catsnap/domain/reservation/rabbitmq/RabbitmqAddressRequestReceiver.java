package net.catsnap.domain.reservation.rabbitmq;

import lombok.RequiredArgsConstructor;
import net.catsnap.domain.reservation.dto.LegalAddressEntity;
import net.catsnap.domain.reservation.entity.Reservation;
import net.catsnap.domain.reservation.repository.ReservationRepository;
import net.catsnap.domain.reservation.service.LocationService;
import net.catsnap.global.Exception.authority.ResourceNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RabbitmqAddressRequestReceiver implements AddressRequestReceiver {

    private final LocationService locationService;
    private final ReservationRepository reservationRepository;

    @Override
    @Transactional
    public void receiveAddressRequest(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new ResourceNotFoundException("해당 예약을 찾을 수 없습니다."));

        LegalAddressEntity legalAddressEntity = locationService.getLegalAddressEntity(
            reservation.getLocation().getY(),
            reservation.getLocation().getX());

        reservation.updateCityLevel(legalAddressEntity.cityLevel());
        reservation.updateDistrictLevel(legalAddressEntity.districtLevel());
        reservation.updateTownLevel(legalAddressEntity.townLevel());
    }
}
