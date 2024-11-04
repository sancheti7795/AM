package com.example.demo.services;

import com.example.demo.daos.EmailDetails;

public interface EmailService {
	
	public String sendEmail(EmailDetails emailDetails);

}
