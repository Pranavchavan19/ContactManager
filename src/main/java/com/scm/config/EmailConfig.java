
package com.scm.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Value("${EMAIL_HOST}")
    private String host;

    @Value("${EMAIL_PORT}")
    private int port;

    @Value("${EMAIL_USERNAME}")
    private String username;

    @Value("${EMAIL_PASSWORD}")
    private String password;

    @PostConstruct
    public void printEnvCheck() {
        System.out.println("📧 EmailConfig env vars:");
        System.out.println("EMAIL_HOST = " + host);
        System.out.println("EMAIL_USERNAME = " + username);
    }

    @Bean
    public JavaMailSender getJavaMailSender() {


        System.out.println("📧 JavaMailSender Bean is initializing...");

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
