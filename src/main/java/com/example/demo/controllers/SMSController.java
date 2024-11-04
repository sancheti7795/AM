package com.example.demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twilio.Twilio;
import com.example.demo.constants.SMSConstant;
import com.example.demo.daos.TwilioRequest;
import com.example.demo.services.SMSService;

@RestController
@RequestMapping("/sms") 
public class SMSController {
	
	private static final Logger log=LoggerFactory.getLogger(SMSController.class);
	
	private final static String ACCOUNT_SID = "AC628c19d8412514dcb41280ae78e4b198"; 
    private final static String AUTH_ID = "33d8f2c494b8c9aa77e4e737bb1993e8"; 
    
    static { 
        Twilio.init(ACCOUNT_SID, AUTH_ID); 
    }
    
    @Autowired
    private SMSService smsService;
	
    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody TwilioRequest twilioRequest) {
        log.info("sendMessage: START - Processing SMS request from {} to {}", 
                 twilioRequest != null ? SMSConstant.fromNumber : "unknown", 
                 twilioRequest != null ? twilioRequest.getToPhoneNumber() : "unknown");

        // Validate the request body for required fields
        if (twilioRequest == null || 
            twilioRequest.getToPhoneNumber() == null || 
            twilioRequest.getMessage() == null) {
            
            log.error("sendMessage: ERROR - Invalid request data: {}", twilioRequest);
            return ResponseEntity.badRequest().body("Invalid request data. Please ensure 'toPhoneNumber', and 'message' are provided.");
        }

        try {
            // Call the service to send the SMS
            String response = smsService.sendMessage(twilioRequest);
            
            log.info("sendMessage: SUCCESS - SMS sent from {} to {}", 
            		SMSConstant.fromNumber, 
                     twilioRequest.getToPhoneNumber());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("sendMessage: ERROR - Failed to send SMS: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to send SMS. Please try again later.");
        }
    }

}
