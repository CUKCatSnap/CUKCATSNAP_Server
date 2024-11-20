package com.cuk.catsnap.global.aws.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.cuk.catsnap.global.aws.s3.dto.PresignedUrlResponse;
import java.net.URL;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AwsS3Client implements ImageClient {

    private final AmazonS3 amazonS3;

    @Value("${spring.aws.s3.presigned-url-expiration}")
    private Integer expirationTime;

    @Value("${spring.aws.s3.bucket-name.root}")
    private String bucketNameRoot;

    @Value("${spring.aws.s3.bucket-name.raw}")
    private String bucketNameRaw;

    @Override
    public PresignedUrlResponse getUploadImageUrl(String fileName) {
        String uuidFileName = addUUIDtoFileName(fileName);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = generatePresignedUrlRequest(
            uuidFileName);
        URL presignedUrl = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        return PresignedUrlResponse.of(presignedUrl, uuidFileName);
    }

    private GeneratePresignedUrlRequest generatePresignedUrlRequest(String fileName) {
        String objectKey = addBucketDirectory(fileName);
        return new GeneratePresignedUrlRequest(bucketNameRoot, objectKey)
            .withMethod(HttpMethod.PUT)
            .withExpiration(new Date(System.currentTimeMillis() + expirationTime));
    }

    private String addUUIDtoFileName(String fileName) {
        return UUID.randomUUID() + fileName;
    }

    private String addBucketDirectory(String fileName) {
        return bucketNameRaw + "/" + fileName;
    }
}
