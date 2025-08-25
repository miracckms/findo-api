package com.findo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Value("${sms.provider:console}")
    private String smsProvider;

    @Value("${sms.account-sid:}")
    private String accountSid;

    @Value("${sms.auth-token:}")
    private String authToken;

    @Value("${sms.from-number:}")
    private String fromNumber;

    @Async
    public void sendVerificationSms(String toPhone, String code) {
        try {
            String message = "Findo doğrulama kodunuz: " + code + ". Bu kod 10 dakika geçerlidir.";

            if ("console".equals(smsProvider)) {
                // For development - just log to console
                System.out.println("SMS to " + toPhone + ": " + message);
            } else if ("twilio".equals(smsProvider)) {
                // Implement Twilio SMS sending
                sendTwilioSms(toPhone, message);
            }
            // Add other SMS providers as needed

        } catch (Exception e) {
            System.err.println("Failed to send SMS: " + e.getMessage());
        }
    }

    @Async
    public void sendPasswordResetSms(String toPhone, String code) {
        try {
            String message = "Findo şifre sıfırlama kodunuz: " + code + ". Bu kod 10 dakika geçerlidir.";

            if ("console".equals(smsProvider)) {
                System.out.println("SMS to " + toPhone + ": " + message);
            } else if ("twilio".equals(smsProvider)) {
                sendTwilioSms(toPhone, message);
            }

        } catch (Exception e) {
            System.err.println("Failed to send password reset SMS: " + e.getMessage());
        }
    }

    private void sendTwilioSms(String toPhone, String message) {
        // Implement Twilio SMS sending logic here
        // This is a placeholder - you would need to add Twilio SDK dependency
        // and implement the actual SMS sending logic

        /*
         * Example implementation:
         * 
         * Twilio.init(accountSid, authToken);
         * Message.creator(
         * new PhoneNumber(toPhone),
         * new PhoneNumber(fromNumber),
         * message
         * ).create();
         */

        System.out.println("Twilio SMS to " + toPhone + ": " + message);
    }
}
