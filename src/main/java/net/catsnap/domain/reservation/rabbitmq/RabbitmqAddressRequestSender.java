package net.catsnap.domain.reservation.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitmqAddressRequestSender implements AddressRequestSender {

    private final RabbitTemplate rabbitTemplate;


    @Override
    public void SendRequestAddress(Long reservationId) {

    }

}
