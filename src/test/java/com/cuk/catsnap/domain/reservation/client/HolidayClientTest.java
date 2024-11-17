package com.cuk.catsnap.domain.reservation.client;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.time.LocalDate;
import java.util.List;
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

@RestClientTest(HolidayClient.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class HolidayClientTest {

    @Autowired
    private HolidayClient holidayClient;

    @Autowired
    private MockRestServiceServer mockServer;

    @Value("${spring.client.holiday-api.base-url}")
    String baseUrl;

    @Value("${spring.client.holiday-api.secret-key}")
    String apiKey;

    @BeforeEach
    void setUp() {
        String expectedResult1 =
            """
                {
                    "response": {
                        "header": {
                            "resultCode": "00",
                            "resultMsg": "NORMAL SERVICE."
                        },
                        "body": {
                            "items": {
                                "item": [
                                    {
                                        "dateKind": "01",
                                        "dateName": "1월1일",
                                        "isHoliday": "Y",
                                        "locdate": 20240101,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "설날",
                                        "isHoliday": "Y",
                                        "locdate": 20240209,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "설날",
                                        "isHoliday": "Y",
                                        "locdate": 20240210,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "설날",
                                        "isHoliday": "Y",
                                        "locdate": 20240211,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "대체공휴일(설날)",
                                        "isHoliday": "Y",
                                        "locdate": 20240212,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "삼일절",
                                        "isHoliday": "Y",
                                        "locdate": 20240301,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "국회의원선거",
                                        "isHoliday": "Y",
                                        "locdate": 20240410,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "어린이날",
                                        "isHoliday": "Y",
                                        "locdate": 20240505,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "대체공휴일(어린이날)",
                                        "isHoliday": "Y",
                                        "locdate": 20240506,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "부처님오신날",
                                        "isHoliday": "Y",
                                        "locdate": 20240515,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "현충일",
                                        "isHoliday": "Y",
                                        "locdate": 20240606,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "광복절",
                                        "isHoliday": "Y",
                                        "locdate": 20240815,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "추석",
                                        "isHoliday": "Y",
                                        "locdate": 20240916,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "추석",
                                        "isHoliday": "Y",
                                        "locdate": 20240917,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "추석",
                                        "isHoliday": "Y",
                                        "locdate": 20240918,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "임시공휴일",
                                        "isHoliday": "Y",
                                        "locdate": 20241001,
                                        "seq": 2
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "개천절",
                                        "isHoliday": "Y",
                                        "locdate": 20241003,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "한글날",
                                        "isHoliday": "Y",
                                        "locdate": 20241009,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "기독탄신일",
                                        "isHoliday": "Y",
                                        "locdate": 20241225,
                                        "seq": 1
                                    }
                                ]
                            },
                            "numOfRows": 100,
                            "pageNo": 1,
                            "totalCount": 19
                        }
                    }
                } 
                """;

        String expectedResult2 =
            """
                {
                    "response": {
                        "header": {
                            "resultCode": "00",
                            "resultMsg": "NORMAL SERVICE."
                        },
                        "body": {
                            "items": {
                                "item": [
                                    {
                                        "dateKind": "01",
                                        "dateName": "1월1일",
                                        "isHoliday": "Y",
                                        "locdate": 20250101,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "설날",
                                        "isHoliday": "Y",
                                        "locdate": 20250128,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "설날",
                                        "isHoliday": "Y",
                                        "locdate": 20250129,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "설날",
                                        "isHoliday": "Y",
                                        "locdate": 20250130,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "삼일절",
                                        "isHoliday": "Y",
                                        "locdate": 20250301,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "대체공휴일",
                                        "isHoliday": "Y",
                                        "locdate": 20250303,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "어린이날",
                                        "isHoliday": "Y",
                                        "locdate": 20250505,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "부처님오신날",
                                        "isHoliday": "Y",
                                        "locdate": 20250505,
                                        "seq": 2
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "대체공휴일",
                                        "isHoliday": "Y",
                                        "locdate": 20250506,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "현충일",
                                        "isHoliday": "Y",
                                        "locdate": 20250606,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "광복절",
                                        "isHoliday": "Y",
                                        "locdate": 20250815,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "개천절",
                                        "isHoliday": "Y",
                                        "locdate": 20251003,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "추석",
                                        "isHoliday": "Y",
                                        "locdate": 20251005,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "추석",
                                        "isHoliday": "Y",
                                        "locdate": 20251006,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "추석",
                                        "isHoliday": "Y",
                                        "locdate": 20251007,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "대체공휴일",
                                        "isHoliday": "Y",
                                        "locdate": 20251008,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "한글날",
                                        "isHoliday": "Y",
                                        "locdate": 20251009,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "기독탄신일",
                                        "isHoliday": "Y",
                                        "locdate": 20251225,
                                        "seq": 1
                                    }
                                ]
                            },
                            "numOfRows": 100,
                            "pageNo": 1,
                            "totalCount": 18
                        }
                    }
                }
                """;

        String expectedResult3 =
            """
                {
                    "response": {
                        "header": {
                            "resultCode": "00",
                            "resultMsg": "NORMAL SERVICE."
                        },
                        "body": {
                            "items": {
                                "item": [
                                    {
                                        "dateKind": "01",
                                        "dateName": "1월1일",
                                        "isHoliday": "Y",
                                        "locdate": 20260101,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "설날",
                                        "isHoliday": "Y",
                                        "locdate": 20260216,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "설날",
                                        "isHoliday": "Y",
                                        "locdate": 20260217,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "설날",
                                        "isHoliday": "Y",
                                        "locdate": 20260218,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "삼일절",
                                        "isHoliday": "Y",
                                        "locdate": 20260301,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "대체공휴일(삼일절)",
                                        "isHoliday": "Y",
                                        "locdate": 20260302,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "어린이날",
                                        "isHoliday": "Y",
                                        "locdate": 20260505,
                                        "seq": 2
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "부처님오신날",
                                        "isHoliday": "Y",
                                        "locdate": 20260524,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "대체공휴일(부처님오신날)",
                                        "isHoliday": "Y",
                                        "locdate": 20260525,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "전국동시지방선거",
                                        "isHoliday": "Y",
                                        "locdate": 20260603,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "현충일",
                                        "isHoliday": "Y",
                                        "locdate": 20260606,
                                        "seq": 2
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "광복절",
                                        "isHoliday": "Y",
                                        "locdate": 20260815,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "대체공휴일(광복절)",
                                        "isHoliday": "Y",
                                        "locdate": 20260817,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "추석",
                                        "isHoliday": "Y",
                                        "locdate": 20260924,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "추석",
                                        "isHoliday": "Y",
                                        "locdate": 20260925,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "추석",
                                        "isHoliday": "Y",
                                        "locdate": 20260926,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "개천절",
                                        "isHoliday": "Y",
                                        "locdate": 20261003,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "대체공휴일(개천절)",
                                        "isHoliday": "Y",
                                        "locdate": 20261005,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "한글날",
                                        "isHoliday": "Y",
                                        "locdate": 20261009,
                                        "seq": 1
                                    },
                                    {
                                        "dateKind": "01",
                                        "dateName": "기독탄신일",
                                        "isHoliday": "Y",
                                        "locdate": 20261225,
                                        "seq": 1
                                    }
                                ]
                            },
                            "numOfRows": 100,
                            "pageNo": 1,
                            "totalCount": 20
                        }
                    }
                }
                """;
        mockServer.expect(
                requestTo(baseUrl + "?serviceKey=" + apiKey + "&solYear=" + "2024"
                    + "&numOfRows=100&_type=json"))
            .andRespond(withSuccess(expectedResult1, MediaType.APPLICATION_JSON));
        mockServer.expect(
                requestTo(baseUrl + "?serviceKey=" + apiKey + "&solYear=" + "2025"
                    + "&numOfRows=100&_type=json"))
            .andRespond(withSuccess(expectedResult2, MediaType.APPLICATION_JSON));
        mockServer.expect(
                requestTo(baseUrl + "?serviceKey=" + apiKey + "&solYear=" + "2026"
                    + "&numOfRows=100&_type=json"))
            .andRespond(withSuccess(expectedResult3, MediaType.APPLICATION_JSON));
    }

    @Test
    void 공휴일_API_호출() {
        // given,when
        List<LocalDate> localDateList = holidayClient.getHolidays();

        //then
        Assertions.assertThat(localDateList.size()).isNotEqualTo(0);
        Assertions.assertThat(localDateList.get(0)).isAfterOrEqualTo(LocalDate.now());
        Assertions.assertThat(localDateList.get(localDateList.size() - 1))
            .isBeforeOrEqualTo(LocalDate.now().plusYears(1));
    }
}