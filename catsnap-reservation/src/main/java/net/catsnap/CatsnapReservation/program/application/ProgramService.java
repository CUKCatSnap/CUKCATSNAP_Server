package net.catsnap.CatsnapReservation.program.application;

import net.catsnap.CatsnapReservation.program.domain.Program;
import net.catsnap.CatsnapReservation.program.application.dto.request.ProgramCreateRequest;
import net.catsnap.CatsnapReservation.program.application.dto.response.ProgramResponse;
import net.catsnap.CatsnapReservation.program.infrastructure.repository.ProgramRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Program 애그리거트의 Application Service
 *
 * <p>도메인 계층의 객체들을 오케스트레이션하여 비즈니스 유스케이스를 구현합니다.
 */
@Service
public class ProgramService {

    private final ProgramRepository programRepository;

    public ProgramService(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    /**
     * 프로그램 생성 유스케이스
     * <p>
     * 원시 타입을 도메인 엔티티에 전달하고, 도메인 엔티티가 VO 생성 및 검증을 담당합니다.
     *
     * @param photographerId 작가 ID
     * @param request        프로그램 생성 요청 정보
     * @return 생성된 프로그램 응답
     */
    @Transactional
    public ProgramResponse createProgram(Long photographerId, ProgramCreateRequest request) {
        Program program = Program.create(
            photographerId,
            request.title(),
            request.description(),
            request.price(),
            request.durationMinutes()
        );

        Program savedProgram = programRepository.save(program);

        return new ProgramResponse(savedProgram.getId());
    }
}
