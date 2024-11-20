package com.cuk.catsnap.global.aws.s3;

import com.cuk.catsnap.global.aws.s3.dto.PresignedUrlResponse;

public interface ImageClient {

    PresignedUrlResponse getUploadImageUrl(String fileName);
}
