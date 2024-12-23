package net.catsnap.domain.client;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import net.catsnap.domain.client.dto.LegalAddress;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

@RestClientTest(ReverseGeocodingClient.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ReverseGeocodingClientTest {

    @Value("${naver.reverse-geocoding.url}")
    private String baseUrl;

    @Autowired
    private ReverseGeocodingClient reverseGeocodingClient;

    @Autowired
    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {

    }

    @Test
    void 위도_경도로_법정주소_조회_성공() {
        // given
        String expectedResult = """
            {
                "status": {
                    "code": 0,
                    "name": "ok",
                    "message": "done"
                },
                "results": [
                    {
                        "name": "legalcode",
                        "code": {
                            "id": "1111011900",
                            "type": "L",
                            "mappingId": "09110119"
                        },
                        "region": {
                            "area0": {
                                "name": "kr",
                                "coords": {
                                    "center": {
                                        "crs": "",
                                        "x": 0.0,
                                        "y": 0.0
                                    }
                                }
                            },
                            "area1": {
                                "name": "서울특별시",
                                "coords": {
                                    "center": {
                                        "crs": "EPSG:4326",
                                        "x": 126.978388,
                                        "y": 37.56661
                                    }
                                },
                                "alias": "서울"
                            },
                            "area2": {
                                "name": "종로구",
                                "coords": {
                                    "center": {
                                        "crs": "EPSG:4326",
                                        "x": 126.978835,
                                        "y": 37.573521
                                    }
                                }
                            },
                            "area3": {
                                "name": "세종로",
                                "coords": {
                                    "center": {
                                        "crs": "EPSG:4326",
                                        "x": 126.976757,
                                        "y": 37.580285
                                    }
                                }
                            },
                            "area4": {
                                "name": "",
                                "coords": {
                                    "center": {
                                        "crs": "",
                                        "x": 0.0,
                                        "y": 0.0
                                    }
                                }
                            }
                        }
                    }
                ]
            }
            """;
        double latitude = 37.56661;
        double longitude = 126.978388;
        mockServer.expect(
                requestTo(baseUrl + "?coords=126.978388,37.56661&output=json&orders=legalcode")
            )
            .andRespond(withSuccess(expectedResult, MediaType.APPLICATION_JSON));

        // when
        LegalAddress legalAddress = reverseGeocodingClient.getLegalAddress(latitude, longitude);

        // then
        Assertions.assertThat(legalAddress.getLevel0()).isEqualTo("서울특별시");
        Assertions.assertThat(legalAddress.getLevel1()).isEqualTo("종로구");
        Assertions.assertThat(legalAddress.getLevel2()).isEqualTo("세종로");
    }

    @Test
    void 대한민국_영토가_아니여서_조회_실패() {
        // given
        String expectedResult = """
            {
                "status": {
                    "code": 3,
                    "name": "no results",
                    "message": "요청한 데이타의 결과가 없습니다."
                },
                "results": []
            }
            """;
        mockServer.expect(
                requestTo(baseUrl + "?coords=0.0,0.0&output=json&orders=legalcode")
            )
            .andRespond(withSuccess(expectedResult, MediaType.APPLICATION_JSON));
        double latitude = 0.0;
        double longitude = 0.0;

        // when, then
        Assertions.assertThatThrownBy(
            () -> reverseGeocodingClient.getLegalAddress(latitude, longitude)
        ).isInstanceOf(RuntimeException.class);
    }
}