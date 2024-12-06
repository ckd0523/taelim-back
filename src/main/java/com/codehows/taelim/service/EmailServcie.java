package com.codehows.taelim.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailServcie {

    private final JavaMailSender mailSender;
    private final EmailSetService emailSetService;

    public void sendEmail(String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        String email = emailSetService.getSelectedEmailSet().getSetEmail();

        helper.setFrom("admin.portal@taelim.co.kr");  // 보내는 사람 설정
        helper.setTo("email");
        //제목
        helper.setSubject(subject);
        //내용
        helper.setText(body, true);

        mailSender.send(message);
    }
}
