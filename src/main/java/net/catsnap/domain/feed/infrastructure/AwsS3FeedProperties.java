package net.catsnap.domain.feed.infrastructure;

import lombok.Getter;
import lombok.Setter;
import net.catsnap.global.aws.s3.AwsS3Properties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@Qualifier("awsS3FeedProperties")
@ConfigurationProperties(prefix = "spring.aws.s3.feed")
public class AwsS3FeedProperties implements AwsS3Properties {

    private String region;
    private String bucketName;
    private String rawImageFolder;
    private String resizedImageFolder;
    private Integer presignedUrlExpiration;
}
