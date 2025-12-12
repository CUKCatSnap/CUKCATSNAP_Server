package net.catsnap.CatsnapAuthorization.model.dto.request;

import java.time.LocalDate;

public record ModelSignUpRequest(
    String identifier,
    String password,
    String nickname,
    LocalDate birthday,
    String phoneNumber
) {

}
