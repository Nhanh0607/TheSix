package poly.edu.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import poly.edu.service.MailerService;

@Service
public class MailerServiceImpl implements MailerService {
    @Autowired
    JavaMailSender sender;

    @Override
    public void send(String to, String subject, String body) throws MessagingException {
        // Tạo message
        MimeMessage message = sender.createMimeMessage();

        // Sử dụng Helper để thiết lập các thông tin
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

        helper.setFrom("The Six Cyber Shop <poly@fpt.edu.vn>");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true); // true = cho phép HTML

        // Gửi
        sender.send(message);
    }
}