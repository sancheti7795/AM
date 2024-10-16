package com.example.demo.security;

import java.util.Date;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.security.Key;
import io.jsonwebtoken.security.Keys;

import com.example.demo.constant.SecurityConstant;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Component
public class JWTGenerator {
	
	private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
	
	public String generateToken(Authentication authentication) {
		String username=authentication.getName();
		Date currentDate=new Date();
		Date expiryDate=new Date(currentDate.getTime()+ SecurityConstant.JWT_EXPIRATION);
		
		String token=Jwts.builder()
				.setSubject(username)
				.setIssuedAt(currentDate)
				.setExpiration(expiryDate)
				.signWith(key, SignatureAlgorithm.HS512).compact();
		
		
		return token;
	}
	
	public String getUsernameFromJWT(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(SecurityConstant.JWT_SECRET)
				.parseClaimsJws(token)
				.getBody();
		
		return claims.getSubject();
	}
	
	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(SecurityConstant.JWT_SECRET).parseClaimsJws(token);
			return true;
		}
		catch(Exception e) {
			throw new AuthenticationCredentialsNotFoundException("JWT expired or incorrect!");
		}
	}

}
