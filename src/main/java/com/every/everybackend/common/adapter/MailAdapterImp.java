package com.every.everybackend.common.adapter;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Async
@Component
@Profile("!test")
public class MailAdapterImp implements MailAdapter {

    private final JavaMailSender javaMailSender;
    private final String from;

    public MailAdapterImp(JavaMailSender javaMailSender,
                          @Value("${spring.mail.username}")
                          String from) {
        this.javaMailSender = javaMailSender;
        this.from = from;
    }

    @Override
    public void sendMail(String to, String subject, String text) {

        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
