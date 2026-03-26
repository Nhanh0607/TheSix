package poly.edu.service;

import jakarta.mail.MessagingException;

public interface MailerService {
    // Gửi email cơ bản
    void send(String to, String subject, String body) throws MessagingException;
}