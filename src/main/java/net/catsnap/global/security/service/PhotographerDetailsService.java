package net.catsnap.global.security.service;

import net.catsnap.domain.photographer.entity.Photographer;
import net.catsnap.domain.photographer.repository.PhotographerRepository;
import net.catsnap.global.security.userdetail.PhotographerDetails;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
