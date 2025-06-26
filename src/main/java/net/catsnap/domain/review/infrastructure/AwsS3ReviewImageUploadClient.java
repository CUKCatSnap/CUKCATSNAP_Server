package net.catsnap.domain.review.infrastructure;

import com.amazonaws.services.s3.AmazonS3;
import net.catsnap.global.aws.s3.AwsS3UploadClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("reviewImageUploadClient")
public class AwsS3ReviewImageUploadClient extends AwsS3UploadClient {

    protected AwsS3ReviewImageUploadClient(AmazonS3 amazonS3,
        AwsS3ReviewProperties awsS3ReviewProperties) {
        super(amazonS3, awsS3ReviewProperties);
    }
}
