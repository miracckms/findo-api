package com.findo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:noreply@findo.com}")
    private String fromEmail;

    @Async
    public void sendVerificationEmail(String toEmail, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Findo - Email Doğrulama");
            message.setText("Email adresinizi doğrulamak için aşağıdaki linke tıklayın:\n\n" +
                    "http://localhost:3000/verify-email?token=" + token + "\n\n" +
                    "Bu link 24 saat geçerlidir.");

            mailSender.send(message);
        } catch (Exception e) {
            // Log error but don't throw exception to avoid breaking the registration flow
            System.err.println("Failed to send verification email: " + e.getMessage());
        }
    }

    @Async
    public void sendPasswordResetEmail(String toEmail, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Findo - Şifre Sıfırlama");
            message.setText("Şifrenizi sıfırlamak için aşağıdaki linke tıklayın:\n\n" +
                    "http://localhost:3000/reset-password?token=" + token + "\n\n" +
                    "Bu link 1 saat geçerlidir.");

            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send password reset email: " + e.getMessage());
        }
    }

    @Async
    public void sendAdApprovedEmail(String toEmail, String adTitle) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Findo - İlanınız Onaylandı");
            message.setText("'" + adTitle + "' başlıklı ilanınız onaylanmış ve yayına alınmıştır.\n\n" +
                    "İlanınızı görüntülemek için Findo'ya giriş yapın.");

            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send ad approved email: " + e.getMessage());
        }
    }

    @Async
    public void sendAdRejectedEmail(String toEmail, String adTitle, String reason) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Findo - İlanınız Reddedildi");
            message.setText("'" + adTitle + "' başlıklı ilanınız reddedilmiştir.\n\n" +
                    "Red sebebi: " + reason + "\n\n" +
                    "İlanınızı düzenleyip tekrar gönderebilirsiniz.");

            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send ad rejected email: " + e.getMessage());
        }
    }
}
