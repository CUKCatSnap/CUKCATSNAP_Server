package net.catsnap.domain.review.infrastructure;

import lombok.Getter;
import lombok.Setter;
import net.catsnap.global.aws.s3.AwsS3Properties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.aws.s3.review")
public class AwsS3ReviewProperties implements AwsS3Properties {

    private String region;
    private String bucketName;
    private String rawImageFolder;
    private String resizedImageFolder;
    private Integer presignedUrlExpiration;
}
