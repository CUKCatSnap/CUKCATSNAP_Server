package net.catsnap.domain.reservation.service;

import net.catsnap.domain.photographer.entity.Photographer;
import net.catsnap.domain.photographer.repository.PhotographerRepository;
import net.catsnap.domain.reservation.dto.PhotographerProgramListResponse;
import net.catsnap.domain.reservation.dto.PhotographerProgramResponse;
import net.catsnap.domain.reservation.dto.photographer.request.ProgramRequest;
import net.catsnap.domain.reservation.dto.photographer.response.photographerProgramIdResponse;
import net.catsnap.domain.reservation.entity.Program;
import net.catsnap.domain.reservation.repository.ProgramRepository;
import net.catsnap.global.Exception.authority.OwnershipNotFoundException;
import net.catsnap.global.security.contextholder.GetAuthenticationInfo;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
        ProgramRequest programRequest, Long programId) {
        Long photographerId = GetAuthenticationInfo.getUserId();
        Photographer photographer = photographerRepository.getReferenceById(photographerId);
        Program program = programRequest.toEntity(photographer);
        if (programId != null) {
            softDeleteProgram(programId);
        }
        Program savedProgram = programRepository.save(program);

        return photographerProgramIdResponse.from(savedProgram);
    }

    public void softDeleteProgram(Long programId) {
        Long photographerId = GetAuthenticationInfo.getUserId();
        programRepository.findById(programId).ifPresentOrElse(
            p -> {
                if (p.getPhotographer().getId().equals(photographerId)) {
                    p.softDelete();
                } else {
                    throw new OwnershipNotFoundException("내가 소유한 프로그램 중, 해당 프로그램을 찾을 수 없습니다.");
                }
            }, () -> {
                throw new OwnershipNotFoundException("내가 소유한 프로그램 중, 해당 프로그램을 찾을 수 없습니다.");
            });
    }

    public PhotographerProgramListResponse getMyProgramList() {
        Long photographerId = GetAuthenticationInfo.getUserId();
        List<Program> programList = programRepository.findByPhotographerIdAndDeletedFalse(
            photographerId);
        return PhotographerProgramListResponse.from(
            programList.stream()
                .map(PhotographerProgramResponse::from)
                .toList()
        );
    }
}
