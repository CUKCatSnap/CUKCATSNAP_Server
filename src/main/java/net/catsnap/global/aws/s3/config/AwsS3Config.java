package net.catsnap.global.aws.s3.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsS3Config {

    @Value("${spring.aws.s3.access-key}")
    String s3AccessKey;

    @Value("${spring.aws.s3.secret-key}")
    String s3SecretKey;

    @Value("${spring.aws.s3.region}")
    String s3Region;

    @Bean
    public AmazonS3 amazonS3Client() {
        return AmazonS3ClientBuilder.standard()
            .withCredentials(
                new AWSStaticCredentialsProvider(new BasicAWSCredentials(s3AccessKey, s3SecretKey))
            )
            .withRegion(s3Region)
            .build();
    }
}
