package net.catsnap.global.jsonformat.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class JsonConfig {

    /*
     * ObjectMapper 빈 등록
     * - JavaTimeModule 등록 -> 등록을 하지 않으면 LocalDateTime, LocalDate, LocalTime 등의 시간 관련 클래스를 직,역직렬화할 때 에러가 발생함.
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }
}
