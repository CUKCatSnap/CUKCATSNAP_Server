package com.cuk.catsnap.domain.photographer.repository;

import com.cuk.catsnap.domain.photographer.entity.Photographer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhotographerRepository extends JpaRepository<Photographer, Long> {

    Optional<Photographer> findByIdentifier(String identifier);
}
