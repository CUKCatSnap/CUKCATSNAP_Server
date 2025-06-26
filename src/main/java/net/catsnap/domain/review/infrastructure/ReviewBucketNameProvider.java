package net.catsnap.domain.review.infrastructure;

import lombok.RequiredArgsConstructor;
import net.catsnap.global.aws.s3.AwsS3BucketNameProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewBucketNameProvider implements AwsS3BucketNameProvider {

    @Value("${spring.aws.s3.bucket-name.root}")
    private String bucketNameRoot;

    @Override
    public String getBucketName() {
        return bucketNameRoot;
    }
}
