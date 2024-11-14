package com.cuk.catsnap.domain.reservation.service;

import com.cuk.catsnap.domain.reservation.dto.PhotographerProgramListResponse;
import com.cuk.catsnap.domain.reservation.dto.PhotographerProgramResponse;
import com.cuk.catsnap.domain.reservation.entity.Program;
import com.cuk.catsnap.domain.reservation.repository.ProgramRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ProgramService {

    private final ProgramRepository programRepository;

    public PhotographerProgramListResponse getPhotographerProgram(Long photographerId) {
        List<Program> programList = programRepository.findByPhotographerIdAndDeletedFalse(
            photographerId);
        return PhotographerProgramListResponse.from(
            programList.stream()
                .map(PhotographerProgramResponse::from)
                .toList());
    }
}
