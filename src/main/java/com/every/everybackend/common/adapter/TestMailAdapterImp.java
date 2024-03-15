package com.every.everybackend.common.adapter;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("test")
@Component
public class TestMailAdapterImp implements MailAdapter{
    @Override
    public void sendMail(String to, String subject, String text) {
        System.out.println("send verification mail to " + to + " with subject: " + subject + " and text: " + text + " in test mode.");
    }
}
