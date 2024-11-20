package com.cuk.catsnap.global.aws.s3.dto;

import java.net.URL;

public record PresignedUrlResponse(
    URL presignedURL,
    String uuidFileName
) {

    public static PresignedUrlResponse of(URL presignedURL, String uuidFileName) {
        return new PresignedUrlResponse(presignedURL, uuidFileName);
    }
}
