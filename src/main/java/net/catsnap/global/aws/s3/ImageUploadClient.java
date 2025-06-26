package net.catsnap.global.aws.s3;

import net.catsnap.global.aws.s3.dto.PresignedUrlResponse;

public interface ImageUploadClient {

    public PresignedUrlResponse getUploadImageUrl(String fileName,
        AwsS3BucketNameProvider awsS3BucketNameProvider);
}
