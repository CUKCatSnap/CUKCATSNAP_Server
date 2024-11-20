package com.cuk.catsnap.global.aws.s3;

import java.net.URL;

public interface ImageClient {

    URL getUploadImageUrl(String fileName);
}
