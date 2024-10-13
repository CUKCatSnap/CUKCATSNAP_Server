package com.cuk.catsnap.domain.photographer.converter;

import com.cuk.catsnap.domain.photographer.dto.PhotographerRequest;
import com.cuk.catsnap.domain.photographer.entity.Photographer;
import org.springframework.stereotype.Component;

@Component
public class PhotographerConverter {

    public Photographer photographerSignUpToPhotographer(PhotographerRequest.PhotographerSignUp photographerSignUp, String encodedPassword) {
        return Photographer.builder()
                .identifier(photographerSignUp.getIdentifier())
                .password(encodedPassword)
                .birthday(photographerSignUp.getBirthday())
                .nickname(photographerSignUp.getNickname())
                .phoneNumber(photographerSignUp.getPhoneNumber())
                .build();
    }
}
