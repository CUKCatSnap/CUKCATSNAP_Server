package net.catsnap.global.aws.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import java.net.URL;
import java.util.Date;
import java.util.UUID;
import net.catsnap.global.aws.s3.dto.PresignedUrlResponse;

public abstract class AwsS3UploadClient implements ImageUploadClient {

    private final AmazonS3 amazonS3;
    private final AwsS3Properties awsS3Properties;

    protected AwsS3UploadClient(AmazonS3 amazonS3, AwsS3Properties awsS3Properties) {
        this.amazonS3 = amazonS3;
        this.awsS3Properties = awsS3Properties;
    }

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
        return new GeneratePresignedUrlRequest(awsS3Properties.getBucketName(), objectKey)
            .withMethod(HttpMethod.PUT)
            .withExpiration(
                new Date(System.currentTimeMillis() + awsS3Properties.getPresignedUrlExpiration()));
    }

    private String addUUIDtoFileName(String fileName) {
        return UUID.randomUUID() + fileName;
    }

    private String addBucketDirectory(String fileName) {
        return awsS3Properties.getRawImageFolder() + "/" + fileName;
    }
}
