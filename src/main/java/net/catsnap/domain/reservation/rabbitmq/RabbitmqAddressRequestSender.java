package net.catsnap.domain.reservation.rabbitmq;

import lombok.RequiredArgsConstructor;
import net.catsnap.global.rabbitmq.RabbitmqConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitmqAddressRequestSender implements AddressRequestSender {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendRequestAddress(Long reservationId) {
        rabbitTemplate.convertAndSend(RabbitmqConfig.ADDRESS_REQUEST_QUEUE, reservationId);
    }
}
