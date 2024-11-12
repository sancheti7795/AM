package com.example.demo.services;

import com.example.demo.daos.LoginDTO;
import com.example.demo.daos.ResetPasswordRequest;

public interface AuthService {

	public String generateOTP(String username);

	String resetPassword(ResetPasswordRequest resetPasswordRequest);

}
