package net.catsnap.domain.review.infrastructure;

import net.catsnap.global.aws.s3.AwsS3DownloadClient;
import net.catsnap.global.aws.s3.AwsS3Properties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("reviewImageDownloadClient")
public class AwsS3ReviewImageDownloadClient extends AwsS3DownloadClient {

    protected AwsS3ReviewImageDownloadClient(AwsS3Properties awsS3Properties) {
        super(awsS3Properties);
    }
}
