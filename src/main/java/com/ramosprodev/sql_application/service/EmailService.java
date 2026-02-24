package com.ramosprodev.sql_application.service;

import com.ramosprodev.sql_application.entity.OrderEntity;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

import java.math.BigDecimal;

@Service
public class EmailService {

    private final SesClient sesClient;

    public EmailService(SesClient sesClient) {
        this.sesClient = sesClient;
    }

    /**
     * The EmailService class structures the sendEmail() method, setting its initial format.
     * The HTML is applied to the class for a more modern display, unlike pure text.
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
                                    .html(Content.builder().data(body).build())
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

    // Welcoming message sending method with HTML
    public void sendWelcomeEmail(String to, String username) {
        String htmlBody = """
        <html>
            <body style="background-color: #0a192f; margin: 0; padding: 0; font-family: 'Segoe UI', Arial, sans-serif;">
                <div style="max-width: 600px; margin: 20px auto; background-color: #112240; border-radius: 12px; overflow: hidden; box-shadow: 0 10px 30px rgba(0,0,0,0.5);">
                    <div style="background-color: #020c1b; padding: 30px; text-align: center;">
                        <h1 style="color: #64ffda; margin: 0; font-size: 24px; letter-spacing: 2px;">KRS IT SOLUTIONS</h1>
                    </div>
                    <div style="padding: 40px; color: #ccd6f6;">
                        <h2 style="color: #fff; font-size: 22px;">Hello, %s</h2>
                        <p style="font-size: 16px; line-height: 1.6; color: #8892b0;">
                            Your registration was successful. <br>
                            We are ready to power up your technological journey.
                        </p>
                        <div style="margin-top: 40px; background-color: #172a45; padding: 20px; border-radius: 8px; border-left: 4px solid #007bff;">
                            <p style="margin: 0; font-size: 14px; color: #64ffda;"><strong>KRS Tip</strong></p>
                            <p style="margin: 5px 0 0 0; font-size: 14px;">Keep your account secure. Never share your access credentials with anyone.</p>
                        </div>
                    </div>
                    <div style="padding: 30px; background-color: #0a192f; color: #495670; font-size: 12px; text-align: center;">
                        See you soon,<br>
                        <strong style="color: #8892b0;">KRS IT Solutions Team</strong>
                    </div>
                </div>
            </body>
        </html>
        """.formatted(username.toUpperCase());

        sendEmail(to, "Welcome to KRS IT Solutions", htmlBody);
    }

    // Generated order sending method
    public void sendGeneratedOrder(String to, String username, OrderEntity order) {
        StringBuilder itemsHtml = new StringBuilder();

        order.getItemsList().forEach(item -> {
            itemsHtml.append(String.format("""
            <tr>
                <td style="padding: 12px 0; border-bottom: 1px solid #233554; color: #ccd6f6;">%s</td>
                <td style="padding: 12px 0; border-bottom: 1px solid #233554; color: #8892b0; text-align: center;">$ %.2f</td>
                <td style="padding: 12px 0; border-bottom: 1px solid #233554; color: #8892b0; text-align: center;">%d</td>
                <td style="padding: 12px 0; border-bottom: 1px solid #233554; color: #64ffda; text-align: right;">$ %.2f</td>
            </tr>
            """,
                    item.getName(),
                    item.getCurrentPrice(),
                    item.getQuantity(),
                    item.getTotalPrice()
            ));
        });

        String htmlBody = """
        <html>
            <body style="background-color: #0a192f; margin: 0; padding: 0; font-family: 'Segoe UI', sans-serif;">
                <div style="max-width: 600px; margin: 20px auto; background-color: #112240; border-radius: 12px; overflow: hidden;">
                    <div style="background-color: #020c1b; padding: 30px; text-align: center;">
                        <h1 style="color: #64ffda; margin: 0; font-size: 24px;">KRS IT SOLUTIONS</h1>
                    </div>
                    <div style="padding: 40px; color: #ccd6f6;">
                        <h2 style="color: #fff;">Order Confirmed!</h2>
                        <p style="color: #8892b0;">Hi %s, we have successfully received your order.</p>
                        <table style="width: 100%%; border-collapse: collapse; margin: 30px 0;">
                            <thead>
                                <tr style="color: #8892b0; font-size: 10px; text-transform: uppercase;">
                                    <th style="text-align: left; padding-bottom: 10px;">Product</th>
                                    <th style="text-align: center; padding-bottom: 10px;">Unit</th>
                                    <th style="text-align: center; padding-bottom: 10px;">Qty</th>
                                    <th style="text-align: right; padding-bottom: 10px;">Subtotal</th>
                                </tr>
                            </thead>
                            <tbody>
                                %s
                            </tbody>
                        </table>
                        <div style="text-align: right; margin-top: 20px;">
                            <p style="margin: 0; color: #8892b0;">Total amount paid</p>
                            <h3 style="margin: 5px 0; color: #64ffda; font-size: 24px;">$ %.2f</h3>
                        </div>
                    </div>
                    <div style="padding: 30px; background-color: #0a192f; color: #495670; font-size: 12px; text-align: center;">
                        Best regards,<br>
                        <strong style="color: #8892b0;">KRS IT Solutions Team</strong>
                    </div>
                </div>
            </body>
        </html>
        """.formatted(username.toUpperCase(), itemsHtml.toString(), order.getPurchaseTotalPrice());

        sendEmail(to, "Order Confirmation - KRS IT Solutions", htmlBody);
    }

    public void sendDepositNotification(String to, String username, BigDecimal amount, BigDecimal currentBalance) {
        String htmlBody = """
        <html>
            <body style="background-color: #0a192f; margin: 0; padding: 0; font-family: 'Segoe UI', Arial, sans-serif;">
                <div style="max-width: 600px; margin: 20px auto; background-color: #112240; border-radius: 12px; overflow: hidden; box-shadow: 0 10px 30px rgba(0,0,0,0.5);">
                    <div style="background-color: #020c1b; padding: 30px; text-align: center;">
                        <h1 style="color: #64ffda; margin: 0; font-size: 24px; letter-spacing: 2px;">KRS IT SOLUTIONS</h1>
                    </div>
                    
                    <div style="padding: 40px; color: #ccd6f6;">
                        <h2 style="color: #fff; font-size: 22px;">Deposit Confirmed!</h2>
                        <p style="font-size: 16px; color: #8892b0;">Hello %s, a new deposit has been credited to your account.</p>
                        
                        <div style="margin: 30px 0; background-color: #172a45; padding: 25px; border-radius: 8px; border-left: 4px solid #2ecc71; text-align: center;">
                            <p style="margin: 0; font-size: 14px; color: #8892b0; text-transform: uppercase;">Amount Credited</p>
                            <h2 style="margin: 10px 0; color: #2ecc71; font-size: 32px;">$ %.2f</h2>
                            <p style="margin: 10px 0 0 0; font-size: 14px; color: #ccd6f6;">New Balance: $ %.2f</p>
                        </div>

                        <div style="background-color: #0d1b2a; padding: 15px; border-radius: 8px;">
                            <p style="margin: 0; font-size: 13px; color: #64ffda;"><strong>Security Note:</strong></p>
                            <p style="margin: 5px 0 0 0; font-size: 13px; color: #8892b0;">If you don't recognize this transaction, please contact our support team immediately.</p>
                        </div>
                    </div>

                    <div style="padding: 30px; background-color: #0a192f; color: #495670; font-size: 12px; text-align: center;">
                        Best regards,<br>
                        <strong style="color: #8892b0;">KRS IT Solutions Team</strong>
                    </div>
                </div>
            </body>
        </html>
        """.formatted(username.toUpperCase(), amount, currentBalance);

        sendEmail(to, "Deposit Notification - KRS IT Solutions", htmlBody);
    }
}