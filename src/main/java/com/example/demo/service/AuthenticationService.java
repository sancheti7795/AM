package com.example.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dao.LoginDTO;
import com.example.demo.dao.User;
import com.example.demo.repository.UserRepository;

@Service
public class AuthenticationService {
	
		@Autowired
	 	private  UserRepository userRepository;
	    
		@Autowired
	    private  PasswordEncoder passwordEncoder;
	    
		@Autowired
	    private  AuthenticationManager authenticationManager;

	    public AuthenticationService(
	        UserRepository userRepository,
	        AuthenticationManager authenticationManager,
	        PasswordEncoder passwordEncoder
	    ) {
	        this.authenticationManager = authenticationManager;
	        this.userRepository = userRepository;
	        this.passwordEncoder = passwordEncoder;
	    }

	    public User authenticate(LoginDTO input) {
	        authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                        input.getUsername(),
	                        input.getPassword()
	                )
	        );
	        
	        User u=userRepository.findByUsername(input.getUsername());

	        return u;
	    }
	}

