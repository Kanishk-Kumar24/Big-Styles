package com.ecom.apis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class Mailer {

    @Autowired
    private JavaMailSender mailSender;
    public void sendMail (String reciever,String otp){
        String subject="otp verification";
        SimpleMailMessage message =new SimpleMailMessage();
        message.setFrom("collegebuddy4u@gmail.com");
        message.setTo(reciever);
        message.setSubject(subject);
        String body = " The otp to verify this email is " + otp;
        message.setText(body);
        mailSender.send(message);
    }
}
