package net.catsnap.domain.reservation.client.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import net.catsnap.domain.reservation.document.Holiday;

// RootResponse.java
public record HolidayResponse(
    Response response
) {

    public record Response(
        Header header,
        Body body
    ) {

        public record Header(
            String resultCode,
            String resultMsg
        ) {

        }

        public record Body(
            Items items,
            Integer numOfRows,
            Integer pageNo,
            Integer totalCount
        ) {

            public record Items(
                List<Item> item
            ) {

                public record Item(
                    String dateKind,
                    String dateName,
                    String isHoliday,
                    Integer locdate,
                    Integer seq
                ) {

                }
            }
        }
    }

    public List<Holiday> toEntity() {
        return response.body.items.item.stream()
            .map(item -> new Holiday(parseDate(item.locdate.toString()), item.dateName))
            .toList();
    }

    private LocalDate parseDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(dateString, formatter);
    }
}
