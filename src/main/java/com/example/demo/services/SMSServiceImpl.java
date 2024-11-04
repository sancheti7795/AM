package com.example.demo.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.constants.SMSConstant;
import com.example.demo.daos.TwilioRequest;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class SMSServiceImpl implements SMSService{
	
	private static final Logger log=LoggerFactory.getLogger(SMSServiceImpl.class);

	public String sendMessage(TwilioRequest twilioRequest) {
	    // Extract Request Data 
	    //String fromNumber = twilioRequest.getFromPhoneNumber(); 
	    String toNumber = twilioRequest.getToPhoneNumber(); 
	    String msg = twilioRequest.getMessage(); 

	    // Log the request details
	    log.info("sendMessage: Sending SMS from {} to {}: {}", SMSConstant.fromNumber, toNumber, msg);

	    try {
	        // Create Message to be sent 
	        Message message = Message.creator(
	                new PhoneNumber(toNumber),
	                new PhoneNumber(SMSConstant.fromNumber),
	                msg
	        ).create();

	        log.info("sendMessage: SUCCESS - Message sent successfully, SID: {}", message.getSid());
	        return "Message sent successfully, SID: " + message.getSid();
	    } catch (Exception e) {
	        log.error("sendMessage: ERROR - Failed to send SMS: {}", e.getMessage());
	        return "Failed to send SMS: " + e.getMessage();
	    }
	}


}
