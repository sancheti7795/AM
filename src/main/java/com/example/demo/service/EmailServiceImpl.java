package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.demo.dao.EmailDetails;

@Service
public class EmailServiceImpl implements EmailService{
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	public void sendEmail(EmailDetails emailDetails) {
		SimpleMailMessage simpleMailMessage= new SimpleMailMessage();
		simpleMailMessage.setTo(emailDetails.getRecipient());
		simpleMailMessage.setSubject(emailDetails.getSubject());
		simpleMailMessage.setText(emailDetails.getMsgBody());
		
		javaMailSender.send(simpleMailMessage);
	}

}
