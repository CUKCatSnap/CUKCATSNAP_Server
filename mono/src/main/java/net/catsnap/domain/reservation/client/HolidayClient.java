package net.catsnap.domain.reservation.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Stream;
import net.catsnap.domain.reservation.client.dto.HolidayResponse;
import net.catsnap.domain.reservation.document.Holiday;
import net.catsnap.global.Exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.ResponseSpec;

@Component
public class HolidayClient {

    private final RestClient restClient;
    private final String apiKey;
    private final ObjectMapper objectMapper;

    public HolidayClient(
        @Value("${spring.client.holiday-api.base-url}")
        String baseUrl,
        @Value("${spring.client.holiday-api.secret-key}")
        String apiKey,
        RestClient.Builder restClientBuilder,
        ObjectMapper objectMapper) {
        this.restClient = restClientBuilder
            .baseUrl(baseUrl)
            .build();
        this.apiKey = apiKey;
        this.objectMapper = objectMapper;
    }

    /*
     * 현재부터 1년 동안의 휴일 정보 조회
     */
    public List<Holiday> getHolidays() {
        // 휴일 정보 조회
        YearMonth yearMonth = YearMonth.now();
        return Stream.iterate(yearMonth, ym -> ym.plusYears(1))
            .limit(2)
            .flatMap(ym -> getHolidaysByYear(ym).stream())
            .toList();
    }

    private List<Holiday> getHolidaysByYear(YearMonth yearMonth) {
        // 월별 휴일 정보 조회
        ResponseSpec responseSpec = restClient.get()
            .uri(
                uriBuilder -> uriBuilder
                    .queryParam("serviceKey", apiKey)
                    .queryParam("solYear", yearMonth.getYear())
                    .queryParam("numOfRows", 100)
                    .queryParam("_type", "json")
                    .build()
            )
            .retrieve();
        String body = responseSpec.body(String.class);
        List<Holiday> holidays;
        try {
            HolidayResponse holidayResponse = objectMapper.readValue(body, HolidayResponse.class);
            holidays = holidayResponse.toEntity();
        } catch (JsonProcessingException e) {
            throw new BusinessException("휴일 정보 조회 중 오류가 발생했습니다.");
        }
        return holidays;
    }
}
