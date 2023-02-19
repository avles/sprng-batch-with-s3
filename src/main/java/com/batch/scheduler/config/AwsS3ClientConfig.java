package com.batch.scheduler.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.ContainerCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsS3ClientConfig {

    @Value("${cloud.aws.credentials.accessKey}")
    private String awsId;

    @Value("${cloud.aws.credentials.secretKey}")
    private String awsKey;

    @Value("${cloud.aws.region.static}")
    private String region;


    @Bean(name = "s3Client")
    public AmazonS3 s3Client() {

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsId, awsKey);
        AmazonS3 amazonS3Client = AmazonS3ClientBuilder.standard()
                .withRegion("ap-southeast-1")

                //With MinIo
                //.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:9000", Regions.US_EAST_1.name()))
                //.withCredentials(new AWSStaticCredentialsProvider(awsCredentials))

                //With AWS
                //.withCredentials(new ContainerCredentialsProvider())

                .withPathStyleAccessEnabled(true)
                .build();

        return amazonS3Client;
    }

}
