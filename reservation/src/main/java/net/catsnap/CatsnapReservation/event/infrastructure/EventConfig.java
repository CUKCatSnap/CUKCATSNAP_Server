package net.catsnap.CatsnapReservation.event.infrastructure;

import net.catsnap.shared.application.EventDeserializer;
import net.catsnap.shared.infrastructure.AvroEventDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfig {

    @Bean
    public EventDeserializer eventDeserializer() {
        return new AvroEventDeserializer();
    }
}
