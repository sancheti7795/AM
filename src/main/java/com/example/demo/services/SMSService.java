package com.example.demo.services;

import com.example.demo.daos.TwilioRequest;

public interface SMSService {
	
	public String sendMessage( TwilioRequest twilioRequest);

}
