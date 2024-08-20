package com.example.jobhunter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    MailSender mailSender;
    public void sendEmail(){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("aaa@localhost");
        msg.setSubject("Testing from Spring Boot");
        msg.setText("Hello World from Spring Boot Email");
        mailSender.send(msg);
    }
}
