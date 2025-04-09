package net.catsnap.global.rabbitmq;

import net.catsnap.domain.reservation.rabbitmq.RabbitmqAddressRequestReceiver;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    public static final String ADDRESS_REQUEST_QUEUE = "addressRequestQueue";

    @Bean
    public Queue addressQueue() {
        return new Queue(ADDRESS_REQUEST_QUEUE, false);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    public MessageListenerAdapter listenerAdaptor(RabbitmqAddressRequestReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveAddressRequest");
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
        MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(ADDRESS_REQUEST_QUEUE);
        container.setMessageListener(listenerAdapter);
        container.setConcurrentConsumers(30);
        return container;
    }
}
