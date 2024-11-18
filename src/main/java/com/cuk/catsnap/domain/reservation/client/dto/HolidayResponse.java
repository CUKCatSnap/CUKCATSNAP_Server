package com.cuk.catsnap.domain.reservation.client.dto;

import java.util.List;

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
}
