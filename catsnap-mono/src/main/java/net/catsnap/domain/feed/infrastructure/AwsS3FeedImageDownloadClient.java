package net.catsnap.domain.feed.infrastructure;

import net.catsnap.global.aws.s3.AwsS3DownloadClient;
import net.catsnap.global.aws.s3.AwsS3Properties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("feedImageDownloadClient")
public class AwsS3FeedImageDownloadClient extends AwsS3DownloadClient {

    protected AwsS3FeedImageDownloadClient(
        @Qualifier("awsS3FeedProperties") AwsS3Properties awsS3Properties) {
        super(awsS3Properties);
    }
}
