package net.catsnap.domain.social.dto.response;

import java.util.List;

public record AddressListResponse(
    List<AddressResponse> addressResponseList
) {

    public static AddressListResponse of(
        List<AddressResponse> addressResponseList
    ) {
        return new AddressListResponse(addressResponseList);
    }
}
