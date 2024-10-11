package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dao.LoginDTO;
import com.example.demo.dao.LoginResponse;
import com.example.demo.dao.User;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.JwtService;


@RequestMapping("/auth")
@RestController
public class LoginController {
	
		@Autowired
	 	private  JwtService jwtService;
	    
		@Autowired
	    private  AuthenticationService authenticationService;

	    public LoginController(JwtService jwtService, AuthenticationService authenticationService) {
	        this.jwtService = jwtService;
	        this.authenticationService = authenticationService;
	    }

	    @PostMapping("/login")
	    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginDTO loginUserDto) {
	        User authenticatedUser = authenticationService.authenticate(loginUserDto);

	        String jwtToken = jwtService.generateToken(authenticatedUser);

	        LoginResponse loginResponse = new LoginResponse();
	        loginResponse.setExpiresIn(jwtService.getExpirationTime());
	        loginResponse.setToken(jwtToken);
	        

	        return ResponseEntity.ok(loginResponse);
	    }

}
