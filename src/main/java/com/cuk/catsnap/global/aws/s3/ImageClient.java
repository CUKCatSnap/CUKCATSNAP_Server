package com.cuk.catsnap.global.aws.s3;

import com.cuk.catsnap.global.aws.s3.dto.PresignedUrlResponse;
import java.net.URL;

public interface ImageClient {

    PresignedUrlResponse getUploadImageUrl(String fileName);

    URL getDownloadImageUrl(String savedFileName);
}
