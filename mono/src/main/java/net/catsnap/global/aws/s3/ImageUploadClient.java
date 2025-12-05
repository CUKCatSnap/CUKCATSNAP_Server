package net.catsnap.global.aws.s3;

import net.catsnap.global.aws.s3.dto.PresignedUrlResponse;

public interface ImageUploadClient {

    PresignedUrlResponse getUploadImageUrl(String fileName);
}
