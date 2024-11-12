package com.example.demo.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.daos.LoginDTO;
import com.example.demo.daos.ResetPasswordRequest;
import com.example.demo.daos.TwilioRequest;
import com.example.demo.daos.UserEntity;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.repositories.UserRepository;

@Service
public class AuthServiceImpl implements AuthService {
	
	private static final Logger log=LoggerFactory.getLogger(AuthServiceImpl.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	private OTPService otpService;
	
	@Autowired
	private SMSService smsService;

	@Override
	public String resetPassword(ResetPasswordRequest resetPasswordRequest) {
		 log.info("resetPassword : START - Attempting to reset password for username: {}",resetPasswordRequest.getLoginDTO().getUsername());

		UserEntity existingUser = userRepository.findByUsername(resetPasswordRequest.getLoginDTO().getUsername())
                .orElseThrow(() -> new UserNotFoundException("User with ID " + resetPasswordRequest.getLoginDTO().getUsername() + " not found"));
		
		
		 if (!otpService.verifyOtp(resetPasswordRequest.getLoginDTO().getUsername(), resetPasswordRequest.getOtp())) {
		        return "Invalid or expired OTP";
		    }
		 
		 
		if (!isPasswordEncoded(resetPasswordRequest.getLoginDTO().getPassword())) {
			existingUser.setPassword(passwordEncoder.encode(resetPasswordRequest.getLoginDTO().getPassword()));
        }
		

	   
	    
		userRepository.save(existingUser);

		return "Password updated successfully.";
		
	}
	
	public boolean isPasswordEncoded(String password) {
        return password != null && password.startsWith("{bcrypt}");
    }

	@Override
	public String generateOTP(String username) {
		
		
		Optional<UserEntity> userOptional= userRepository.findByUsername(username);
		UserEntity user=userOptional.get();
		TwilioRequest twilioRequest=new TwilioRequest();
		
		
		 if (user.getContactno() == null || user.getContactno().isEmpty()) {
	            throw new IllegalArgumentException("Contact number not found for the user.");
	        }
		 
		twilioRequest.setToPhoneNumber(user.getContactno());
		
		
		String otp=otpService.generateOTP(username);
		twilioRequest.setMessage("Your OTP is valid for 5 minutes. OTP :  "+otp);
		smsService.sendMessage(twilioRequest);
		
		return "OTP generated : "+otp;
		
	}

}
