package net.catsnap.domain.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.catsnap.domain.client.dto.response.ReverseGeocodingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.ResponseSpec;

@Component
public class ReverseGeocodingClient {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public ReverseGeocodingClient(
        @Value("${naver.client-id}")
        String clientId,
        @Value("${naver.client-secret}")
        String clientSecret,
        @Value(("${naver.reverse-geocoding.url}"))
        String baseUrl,
        RestClient.Builder restClientBuilder,
        ObjectMapper objectMapper
    ) {
        this.restClient = restClientBuilder
            .baseUrl(baseUrl)
            .defaultHeaders(
                httpHeaders -> {
                    httpHeaders.set("x-ncp-apigw-api-key-id", clientId);
                    httpHeaders.set("x-ncp-apigw-api-key", clientSecret);
                }
            )
            .build();
        this.objectMapper = objectMapper;
    }

    public String getLegalAddressCode(double latitude, double longitude) {
        return getAddress(latitude, longitude).getLegalCode();
    }

    private ReverseGeocodingResponse getAddress(double latitude, double longitude) {
        // 위도, 경도를 주소로 변환
        String code = longitude + "," + latitude;
        ResponseSpec responseSpec = restClient.get()
            .uri(
                uriBuilder -> uriBuilder
                    .queryParam("coords", code)
                    .queryParam("output", "json")
                    .queryParam("orders", "legalcode")
                    .build()
            )
            .retrieve()
            .onStatus(
                status -> status.is4xxClientError() || status.is5xxServerError(),
                (httpRequest, clientResponse) -> {
                    throw new RuntimeException("서버와 통신 중 오류가 발생했습니다.");
                }
            );
        String body = responseSpec.body(String.class);
        try {
            return objectMapper.readValue(body, ReverseGeocodingResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("국내 영토만 주소를 찾을 수 있습니다", e);
        }
    }
}
