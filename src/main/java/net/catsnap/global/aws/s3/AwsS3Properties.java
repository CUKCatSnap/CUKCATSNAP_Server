package net.catsnap.global.aws.s3;

public interface AwsS3Properties {

    String getRegion();

    String getBucketName();

    String getRawImageFolder();

    String getResizedImageFolder();

    Integer getPresignedUrlExpiration();
}
