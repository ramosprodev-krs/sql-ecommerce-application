package com.ramosprodev.sql_application.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Service
public class EmailService {

    private final SesClient sesClient;

    public EmailService(SesClient sesClient) {
        this.sesClient = sesClient;
    }

    /**
     * The EmailService class structures the sendEmail() method, setting its initial format.
     * The method is also used inside the controller classes.
     **/

    public void sendEmail(String to, String subject, String body) {
        try {
            SendEmailRequest request = SendEmailRequest.builder()
                    .destination(Destination.builder()
                            .toAddresses(to)
                            .build())
                    .message(Message.builder()
                            .subject(Content.builder().data(subject).build())
                            .body(Body.builder()
                                    .text(Content.builder().data(body).build())
                                    .build())
                            .build())
                    .source("krssender@gmail.com")
                    .build();

            sesClient.sendEmail(request);
            System.out.println("Email sent successfully to: " + to);

        } catch (SesException e) {
            System.err.println("AWS SES Error: " + e.awsErrorDetails().errorMessage());
        }
    }
}