package com.cuk.catsnap.domain.photographer.service;

import com.cuk.catsnap.domain.photographer.document.PhotographerSetting;
import com.cuk.catsnap.domain.photographer.dto.PhotographerRequest;

public interface PhotographerService {

    void singUp(PhotographerRequest.PhotographerSignUp photographerSignUp);
    void initializePhotographerSetting(Long photographerId);
    PhotographerSetting getPhotographerSetting();
    void updatePhotographerSetting(PhotographerRequest.PhotographerSetting photographerSetting);
}
