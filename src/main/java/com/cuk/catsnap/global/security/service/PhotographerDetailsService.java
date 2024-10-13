package com.cuk.catsnap.global.security.service;

import com.cuk.catsnap.domain.photographer.entity.Photographer;
import com.cuk.catsnap.domain.photographer.repository.PhotographerRepository;
import com.cuk.catsnap.global.security.userdetail.PhotographerDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@Transactional
@RequiredArgsConstructor
public class PhotographerDetailsService implements UserDetailsService {

    private final PhotographerRepository photographerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Photographer> photographer = photographerRepository.findByIdentifier(username);
        return photographer.map(PhotographerDetails::new)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Photographer not found")
                );
    }

    public Long getPhotographerId(String username) {
        Optional<Photographer> photographer = photographerRepository.findByIdentifier(username);
        return photographer.map(Photographer::getId)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Photographer not found")
                );
    }
}
