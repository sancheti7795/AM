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
	

    // Generates a JWT token based on the authentication details
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expiryDate = new Date(currentDate.getTime() + SecurityConstant.JWT_EXPIRATION);
        
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expiryDate)
                .signWith(SecurityConstant.JWT_SECRET, SignatureAlgorithm.HS256) // Ensure JWT_SECRET is a Key
                .compact();
    }

    // Retrieves the username from the JWT token
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstant.JWT_SECRET) // Ensure JWT_SECRET is a Key
                .parseClaimsJws(token)
                .getBody();
        
        return claims.getSubject();
    }

    // Validates the JWT token and checks for expiry
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SecurityConstant.JWT_SECRET) // Use the same signing key
                .parseClaimsJws(token);
            return true; // Token is valid
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            System.err.println("JWT expired: " + e.getMessage());
        } catch (io.jsonwebtoken.SignatureException e) {
            System.err.println("Invalid JWT signature: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Invalid JWT: " + e.getMessage());
        }
        
        throw new AuthenticationCredentialsNotFoundException("JWT expired or incorrect!");
    }

}
