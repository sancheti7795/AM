package com.example.demo.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dao.AuthResponseDTO;
import com.example.demo.dao.LoginDTO;
import com.example.demo.dao.RegisterDTO;
import com.example.demo.dao.Role;
import com.example.demo.dao.UserEntity;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JWTGenerator;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JWTGenerator jwtGenerator;
	
	@PostMapping("register")
	public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO) {
		if(userRepository.existsByUsername(registerDTO.getUsername())) {
			return new ResponseEntity<>("Username already exists.",HttpStatus.BAD_REQUEST);
		}
		UserEntity user=new UserEntity();
		user.setUsername(registerDTO.getUsername());
		user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
		
		Role roles=roleRepository.findByName("USER").get();
		user.setRoles(Collections.singletonList(roles));
		
		
		
		userRepository.save(user);
		return new ResponseEntity<>("User registered successfully!", HttpStatus.OK);
	}
	
	
	@PostMapping("login")
	public  ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO loginDTO) {
		
		Authentication authentication =authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token=jwtGenerator.generateToken(authentication);
		return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);
	}
	

}
