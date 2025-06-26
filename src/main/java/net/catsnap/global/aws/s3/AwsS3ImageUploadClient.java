package net.catsnap.global.aws.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import java.net.URL;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import net.catsnap.global.aws.s3.dto.PresignedUrlResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AwsS3ImageUploadClient implements ImageUploadClient {

    private final AmazonS3 amazonS3;

    @Value("${spring.aws.s3.presigned-url-expiration}")
    private Integer expirationTime;

    @Value("${spring.aws.s3.bucket-name.raw}")
    private String rawFileDirectory;

    @Override
    public PresignedUrlResponse getUploadImageUrl(String fileName,
        AwsS3BucketNameProvider awsS3BucketNameProvider) {
        String uuidFileName = addUUIDtoFileName(fileName);

        GeneratePresignedUrlRequest generatePresignedUrlRequest = generatePresignedUrlRequest(
            uuidFileName, awsS3BucketNameProvider);

        URL presignedUrl = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        return PresignedUrlResponse.of(presignedUrl, uuidFileName);
    }

    private GeneratePresignedUrlRequest generatePresignedUrlRequest(String fileName,
        AwsS3BucketNameProvider awsS3BucketNameProvider) {
        String objectKey = addBucketDirectory(fileName);

        return new GeneratePresignedUrlRequest(awsS3BucketNameProvider.getBucketName(), objectKey)
            .withMethod(HttpMethod.PUT)
            .withExpiration(new Date(System.currentTimeMillis() + expirationTime));
    }

    private String addUUIDtoFileName(String fileName) {
        return UUID.randomUUID() + fileName;
    }

    private String addBucketDirectory(String fileName) {
        return rawFileDirectory + "/" + fileName;
    }
}
