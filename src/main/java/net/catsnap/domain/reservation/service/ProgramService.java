package net.catsnap.domain.reservation.service;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.reservation.dto.PhotographerProgramListResponse;
import net.catsnap.domain.reservation.dto.PhotographerProgramResponse;
import net.catsnap.domain.reservation.dto.photographer.request.ProgramRequest;
import net.catsnap.domain.reservation.dto.photographer.response.photographerProgramIdResponse;
import net.catsnap.domain.reservation.entity.Program;
import net.catsnap.domain.reservation.repository.ProgramRepository;
import net.catsnap.domain.user.photographer.entity.Photographer;
import net.catsnap.domain.user.photographer.repository.PhotographerRepository;
import net.catsnap.global.Exception.authority.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ProgramService {

    private final ProgramRepository programRepository;
    private final PhotographerRepository photographerRepository;

    public PhotographerProgramListResponse getPhotographerProgram(Long photographerId) {
        List<Program> programList = programRepository.findByPhotographerIdAndDeletedFalse(
            photographerId);
        return PhotographerProgramListResponse.from(
            programList.stream()
                .map(PhotographerProgramResponse::from)
                .toList());
    }

    /*
     * Program은 수정을 하더라도 기존의 것을 soft delete하고 새로운 것을 생성하는 방식으로 진행한다.
     * 왜냐하면 기존의 Program을 예약한 고객이 있을 수 있기 때문이다.
     */
    public photographerProgramIdResponse createProgram(
        ProgramRequest programRequest, Long programId, long photographerId) {
        Photographer photographer = photographerRepository.getReferenceById(photographerId);
        Program program = programRequest.toEntity(photographer);
        if (programId != null) {
            softDeleteProgram(programId, photographerId);
        }
        Program savedProgram = programRepository.save(program);

        return photographerProgramIdResponse.from(savedProgram);
    }

    public void softDeleteProgram(Long programId, long photographerId) {
        programRepository.findById(programId).ifPresentOrElse(
            p -> {
                p.checkOwnership(photographerId);
                p.softDelete();
            }, () -> {
                throw new ResourceNotFoundException("해당 프로그램을 찾을 수 없습니다.");
            });
    }

    public PhotographerProgramListResponse getMyProgramList(long photographerId) {
        List<Program> programList = programRepository.findByPhotographerIdAndDeletedFalse(
            photographerId);
        return PhotographerProgramListResponse.from(
            programList.stream()
                .map(PhotographerProgramResponse::from)
                .toList()
        );
    }
}
