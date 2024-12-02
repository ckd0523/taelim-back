package com.codehows.taelim.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServcie {

    @Autowired
    private JavaMailSender mailSender;

    private final EmailSetService emailSetService;

    public EmailServcie(EmailSetService emailSetService) {
        this.emailSetService = emailSetService;
    }

    public void sendEmail(String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String email = emailSetService.getSelectedEmailSet().getSetEmail();

        helper.setFrom("태림 메일");  // 보내는 사람 설정
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(body, true);

        mailSender.send(message);
    }
}
