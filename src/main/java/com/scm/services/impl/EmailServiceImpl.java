//package com.scm.services.impl;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//import com.scm.services.EmailService;
//
//@Service
//public class EmailServiceImpl implements EmailService {
//
//    @Autowired
//    private JavaMailSender eMailSender;
//
//    @Value("${spring.mail.properties.domain_name}")
//    private String domainName;
//
//    @Override
//    public void sendEmail(String to, String subject, String body) {
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(body);
//        message.setFrom(domainName);
//        eMailSender.send(message);
//
//    }
//
//    @Override
//    public void sendEmailWithHtml() {
//        // TODO Auto-generated method stub
//        throw new UnsupportedOperationException("Unimplemented method 'sendEmailWithHtml'");
//    }
//
//    @Override
//    public void sendEmailWithAttachment() {
//        // TODO Auto-generated method stub
//        throw new UnsupportedOperationException("Unimplemented method 'sendEmailWithAttachment'");
//    }
//
//}



package com.scm.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.scm.services.EmailService;

@Service
public class EmailServiceImpl implements EmailService {
    

    @Autowired
    private JavaMailSender eMailSender;

    private final String senderEmail = System.getenv("MAIL_USERNAME"); // From Render env

    @Override
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(senderEmail); // fixed here
        eMailSender.send(message);
    }

    @Override
    public void sendEmailWithHtml() {
        throw new UnsupportedOperationException("Unimplemented method 'sendEmailWithHtml'");
    }

    @Override
    public void sendEmailWithAttachment() {
        throw new UnsupportedOperationException("Unimplemented method 'sendEmailWithAttachment'");
    }
}
