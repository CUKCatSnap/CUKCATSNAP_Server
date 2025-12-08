package net.catsnap.domain.feed.infrastructure;

import com.amazonaws.services.s3.AmazonS3;
import net.catsnap.global.aws.s3.AwsS3Properties;
import net.catsnap.global.aws.s3.AwsS3UploadClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("feedImageUploadClient")
public class AwsS3FeedImageUploadClient extends AwsS3UploadClient {

    protected AwsS3FeedImageUploadClient(AmazonS3 amazonS3,
        @Qualifier("awsS3FeedProperties") AwsS3Properties awsS3Properties) {
        super(amazonS3, awsS3Properties);
    }
}
