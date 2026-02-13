package net.catsnap.global.aws.s3;

public interface ImageDownloadClient {

    String getDownloadImageUrl(String savedFileName);
}
