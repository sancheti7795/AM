package com.example.demo.constant;

import javax.crypto.SecretKey;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class SecurityConstant {
	
	public static final long JWT_EXPIRATION=700000;
	
	public static final SecretKey JWT_SECRET=Keys.secretKeyFor(SignatureAlgorithm.HS256);

}
