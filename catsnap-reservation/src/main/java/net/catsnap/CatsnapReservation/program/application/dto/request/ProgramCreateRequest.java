package net.catsnap.CatsnapReservation.program.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 프로그램 생성 요청 DTO
 */
public record ProgramCreateRequest(
    @NotBlank(message = "프로그램 제목은 필수입니다")
    String title,

    String description,

    @NotNull(message = "가격은 필수입니다")
    @Min(value = 0, message = "가격은 0원 이상이어야 합니다")
    Long price,

    @NotNull(message = "소요 시간은 필수입니다")
    @Min(value = 1, message = "소요 시간은 1분 이상이어야 합니다")
    Integer durationMinutes
) {

}
