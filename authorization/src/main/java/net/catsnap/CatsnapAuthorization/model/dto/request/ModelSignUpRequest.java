package net.catsnap.CatsnapAuthorization.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;

public record ModelSignUpRequest(
    @NotBlank(message = "식별자는 필수입니다")
    String identifier,

    @NotBlank(message = "비밀번호는 필수입니다")
    String password,

    @NotBlank(message = "닉네임은 필수입니다")
    String nickname,

    @NotNull(message = "생년월일은 필수입니다")
    @Past(message = "생년월일은 과거 날짜여야 합니다")
    LocalDate birthday,

    @NotNull(message = "생년월일은 필수입니다")
    String phoneNumber
) {

}
