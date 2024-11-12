package com.example.demo.services;

public interface OTPService {

	public String generateOTP(String email);

	public boolean verifyOtp(String username, String otp);
}
