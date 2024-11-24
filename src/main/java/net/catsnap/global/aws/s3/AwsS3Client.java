package net.catsnap.global.aws.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import net.catsnap.global.aws.s3.dto.PresignedUrlResponse;
import java.net.MalformedURLException;
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

    @Value("${spring.aws.s3.region}")
    private String region;

    @Override
    public PresignedUrlResponse getUploadImageUrl(String fileName) {
        String uuidFileName = addUUIDtoFileName(fileName);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = generatePresignedUrlRequest(
            uuidFileName);
        URL presignedUrl = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        return PresignedUrlResponse.of(presignedUrl, uuidFileName);
    }

    @Override
    public URL getDownloadImageUrl(String savedFileName) {
        try {
            String bucketDirectory = addBucketDirectory(savedFileName);
            return new URL(
                "https", // 프로토콜
                bucketNameRoot + ".s3." + region + ".amazonaws.com", // 호스트
                "/" + bucketDirectory // 경로 (앞에 '/' 포함)
            );
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("올바른 URL 형식이 아닙니다.: " + savedFileName, e);
        }
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
