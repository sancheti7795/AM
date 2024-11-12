package com.example.demo.services;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.example.demo.daos.OTPDetails;

@Service
public class OTPServiceImpl implements OTPService {
	
	private final Map<String, OTPDetails> otpStore=new ConcurrentHashMap<>();
	
	
	public String generateOTP(String username) {
		String otp = String.valueOf(1000+ new Random().nextInt(9000));
		OTPDetails otpDetails = new OTPDetails(otp, LocalDateTime.now().plusMinutes(5));
		otpStore.put(username, otpDetails);
		return otp;
	}
	
	public boolean verifyOtp(String username, String otp) {
		System.out.println("============================================================================");
		System.out.println(otpStore);
		System.out.println("============================================================================");
		OTPDetails otpDetails = otpStore.get(username);
        if (otpDetails == null || otpDetails.getExpirationTime().isBefore(LocalDateTime.now())) {
            otpStore.remove(username);
            return false; // OTP expired or not found
        }
        boolean isValid = otpDetails.getOtp().equals(otp);
        if (isValid) {
            otpStore.remove(username); // Remove OTP after successful verification
        }
        return isValid;
    }
	
	

}
