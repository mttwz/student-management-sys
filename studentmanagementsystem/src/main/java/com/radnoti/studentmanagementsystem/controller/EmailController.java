package com.radnoti.studentmanagementsystem.controller;

import com.radnoti.studentmanagementsystem.model.dto.EmailDto;
import com.radnoti.studentmanagementsystem.service.EmailService;
import com.sun.xml.bind.v2.TODO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/mail")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    // TODO: 2023. 04. 24. ez egy test

    /**
     * Sends an email.
     *
     * @throws MessagingException if an error occurs while sending the email.
     */
    @PostMapping(path = "send")
    public void sendEmail() throws MessagingException {
        EmailDto email = new EmailDto();
        email.setTo("mateevoros@gmail.com");
        email.setSubject("Test email");
        email.setText(
                "<html>" +
                    "<body>" +
                        "<h1>Spring test email</h1>" +
                        "<p>Ayyyyyyy 69 420 🐸</p>" +
                    "</body>" +
                "</html>");

        emailService.sendEmail(email);
    }

}
