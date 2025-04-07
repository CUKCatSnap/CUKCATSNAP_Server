package net.catsnap.domain.social.dto.response;

import java.util.List;

public record AddressListResponse(
    List<AddressResponse> addressResponseList
) {

}
