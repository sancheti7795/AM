package com.example.demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.daos.EmailDetails;
import com.example.demo.services.EmailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/email")
public class EmailController {
	
	private static final Logger log=LoggerFactory.getLogger(EmailController.class);
	
	@Autowired
	private EmailService emailService;
	
	@PostMapping("/send")
	public ResponseEntity<String> sendEmail(@RequestBody EmailDetails emailDetails) {
	    log.info("sendEmail : START - Sending email to: {}", emailDetails.getRecipient());

	    String responseMessage = emailService.sendEmail(emailDetails);
	    
	    // Log the response message
	    log.info("sendEmail : RESPONSE - {}", responseMessage);

	    // Determine success or failure from the response message
	    if (responseMessage.startsWith("Email sent successfully")) {
	        return ResponseEntity.ok(responseMessage);
	    } else {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
	    }
	}

	

}
