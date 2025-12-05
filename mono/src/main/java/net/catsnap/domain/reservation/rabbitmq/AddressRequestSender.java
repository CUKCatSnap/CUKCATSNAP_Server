package net.catsnap.domain.reservation.rabbitmq;

public interface AddressRequestSender {

    void sendRequestAddress(Long reservationId);
}
