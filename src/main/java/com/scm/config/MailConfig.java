package com.scm.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();

        sender.setHost(System.getenv("EMAIL_HOST"));
        sender.setPort(Integer.parseInt(System.getenv("EMAIL_PORT")));
        sender.setUsername(System.getenv("EMAIL_USERNAME"));
        sender.setPassword(System.getenv("EMAIL_PASSWORD"));

        Properties props = sender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.transport.protocol", "smtp");

        return sender;
    }
}
