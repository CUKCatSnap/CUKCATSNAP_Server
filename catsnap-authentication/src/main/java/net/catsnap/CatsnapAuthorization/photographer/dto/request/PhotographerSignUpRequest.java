package net.catsnap.CatsnapAuthorization.photographer.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PhotographerSignUpRequest(
    @NotBlank(message = "식별자는 필수입니다")
    String identifier,

    @NotBlank(message = "비밀번호는 필수입니다")
    String password,

    @NotBlank(message = "이름은 필수입니다")
    String name,

    @NotBlank(message = "전화번호는 필수입니다")
    String phoneNumber
) {

}
