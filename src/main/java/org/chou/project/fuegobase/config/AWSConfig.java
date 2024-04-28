package org.chou.project.fuegobase.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfig {
    @Value("${s3.access.key}")
    private String s3AccessKey;
    @Value("${s3.secret.key}")
    private String s3SecretKey;

    //    private AmazonS3 s3Client;
    public AWSCredentials credentials() {
        return new BasicAWSCredentials(s3AccessKey, s3SecretKey);
    }
//
//    @Bean
//    public AmazonS3 amazonS3() {
//        AmazonS3 s3client = AmazonS3ClientBuilder
//                .standard()
//                .withCredentials(new AWSStaticCredentialsProvider(credentials()))
//                .withRegion(Regions.US_EAST_1)
//                .build();
//        return s3client;
//    }

    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials()))
                .withRegion(Regions.US_EAST_1)
                .build();
    }

//    @PostConstruct
//    public void createS3Client() {
//
//        BasicAWSCredentials awsCredentials =
//                new BasicAWSCredentials(s3AccessKey, s3SecretKey);
//        AWSCredentials credentials = awsCredentials;
//        this.s3Client = AmazonS3ClientBuilder
//                .standard()
//                .withCredentials(new AWSStaticCredentialsProvider(credentials))
//                .withRegion(Regions.US_EAST_1)
//                .build();
//    }
//
//    @Bean
//    public AmazonS3 getClient() {
//        return s3Client;
//    }
}
