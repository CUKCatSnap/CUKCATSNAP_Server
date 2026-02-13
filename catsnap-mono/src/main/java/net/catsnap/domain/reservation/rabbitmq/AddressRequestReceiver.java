package net.catsnap.domain.reservation.rabbitmq;

public interface AddressRequestReceiver {

    public void receiveAddressRequest(Long reservationId);
}
