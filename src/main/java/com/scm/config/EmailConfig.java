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
    public void checkEnv() {
        System.out.println("✅ Email config: " + host + " | " + username);
        System.out.println("✅ Email Config loaded");
        System.out.println("✅ Host: " + host);
        System.out.println("✅ Port: " + port);
        System.out.println("✅ Username: " + username);
        System.out.println("✅ Password: " + (password != null ? "✔️ set" : "❌ null"));
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(host);
        sender.setPort(port);
        sender.setUsername(username);
        sender.setPassword(password);

        Properties props = sender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return sender;
    }

}
