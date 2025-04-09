package net.catsnap.domain.reservation.rabbitmq;

public interface AddressRequestSender {

    void SendRequestAddress(Long reservationId);
}
