package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.model.dto.EmailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;


    public void sendEmail(EmailDto email) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo(email.getTo());
        helper.setSubject(email.getSubject());
        helper.setText(email.getText(), true);

        javaMailSender.send(message);
    }

}
