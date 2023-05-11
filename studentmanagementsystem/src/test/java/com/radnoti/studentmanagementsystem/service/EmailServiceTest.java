package com.radnoti.studentmanagementsystem.service;

import com.radnoti.studentmanagementsystem.model.dto.EmailDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mail.javamail.JavaMailSender;



import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.verify;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    public void testSendEmail() throws MessagingException {
        // Arrange
        EmailDto email = new EmailDto();
        email.setTo("test@example.com");
        email.setSubject("Test subject");
        email.setText("Test body");

        JavaMailSender javaMailSender = mock(JavaMailSender.class);
        MimeMessage mimeMessage = mock(MimeMessage.class);

        // Set up the JavaMailSender mock to return the MimeMessage mock when createMimeMessage is called
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Create the EmailService with the JavaMailSender mock
        EmailService emailService = new EmailService(javaMailSender);

        // Act
        emailService.sendEmail(email);

        // Assert
        verify(javaMailSender).send(eq(mimeMessage));
        verify(mimeMessage).setRecipient(eq(javax.mail.Message.RecipientType.TO), eq(new javax.mail.internet.InternetAddress(email.getTo())));
        verify(mimeMessage).setSubject(eq(email.getSubject()));
    }


}
