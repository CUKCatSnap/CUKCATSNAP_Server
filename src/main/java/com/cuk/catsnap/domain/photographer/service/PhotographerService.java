package com.cuk.catsnap.domain.photographer.service;

import com.cuk.catsnap.domain.photographer.dto.PhotographerRequest;

public interface PhotographerService {

    void singUp(PhotographerRequest.PhotographerSignUp photographerSignUp);
}
