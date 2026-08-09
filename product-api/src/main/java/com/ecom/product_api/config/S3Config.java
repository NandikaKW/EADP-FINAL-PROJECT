package com.ecom.product_api.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {
    @Value("${accessKey}")
    private String accessKey;

    @Value("${secretKey}")
    private String secretKey;


    @Value("${region}")
    private String region;

    // AWS SDK v2
    @Bean
    public S3Client s3Client() {

        AwsBasicCredentials credentials =
                AwsBasicCredentials.create(accessKey, secretKey);

        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(credentials)
                )
                .build();
    }


    // AWS SDK v1
    @Bean
    public AmazonS3Client amazonS3Client() {

        BasicAWSCredentials credentials =
                new BasicAWSCredentials(accessKey, secretKey);

        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(
                        new AWSStaticCredentialsProvider(credentials)
                )
                .build();
    }
}
