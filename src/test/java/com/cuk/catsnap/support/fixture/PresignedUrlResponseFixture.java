package com.cuk.catsnap.support.fixture;

import com.cuk.catsnap.global.aws.s3.dto.PresignedUrlResponse;
import java.net.MalformedURLException;
import java.net.URL;

public class PresignedUrlResponseFixture {

    URL presignedURL;
    String uuidFileName = "uuidFileName";

    public static PresignedUrlResponseFixture presignedUrlResponse() {
        return new PresignedUrlResponseFixture();
    }


    public PresignedUrlResponse build() {
        if (this.presignedURL == null) {
            try {
                this.presignedURL = new URL("http://localhost:8080");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return new PresignedUrlResponse(this.presignedURL, this.uuidFileName);
    }
}
