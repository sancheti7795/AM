package com.example.demo.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.demo.daos.EmailDetails;

@Service
public class EmailServiceImpl implements EmailService{
	
	private static final Logger log=LoggerFactory.getLogger(EmailServiceImpl.class);
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	public String sendEmail(EmailDetails emailDetails) {
	    // Validate the email details
	    if (emailDetails == null || 
	        emailDetails.getRecipient() == null || 
	        emailDetails.getSubject() == null || 
	        emailDetails.getMsgBody() == null) {
	        log.error("sendEmail : ERROR - EmailDetails cannot be null or contain null fields.");
	        return "Email details cannot be null or contain null fields.";
	    }

	    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
	    simpleMailMessage.setTo(emailDetails.getRecipient());
	    simpleMailMessage.setSubject(emailDetails.getSubject());
	    simpleMailMessage.setText(emailDetails.getMsgBody());

	    try {
	        javaMailSender.send(simpleMailMessage);
	        log.info("sendEmail : SUCCESS - Email sent to {}", emailDetails.getRecipient());
	        return "Email sent successfully to " + emailDetails.getRecipient() + ".";
	    } catch (MailException ex) {
	        log.error("sendEmail : ERROR - Failed to send email to {}: {}", emailDetails.getRecipient(), ex.getMessage());
	        return "Failed to send email to " + emailDetails.getRecipient() + ": " + ex.getMessage();
	    }
	}


}
