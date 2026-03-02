package com.ramosprodev.sql_application.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

@Configuration
public class SESConfiguration {

    /**
     * This class is responsible for setting the AWS IAM User, in this context, for Amazon SES.
     * By combining the access key, secret key, and aws region, the basic credentials are created and later implemented
     * by the SesClient @Bean.
     **/


    /*
     *  These three attributes use the values contained in the
     *  project's properties file.
     */

    // 1. Gets the access key
    @Value("${aws.access.key.id}")
    private String accessKey;

    // 2. Gets the secret access key
    @Value("${aws.secret.access.key}")
    private String secretKey;

    // 3. Gets the aws region
    @Value("${aws.region}")
    private String awsRegion;


    // The following bean is responsible for setting the provided credentials and returning the sesClient.
    @Bean
    public SesClient sesClient() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        return SesClient.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }
}