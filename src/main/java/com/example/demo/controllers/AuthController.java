package com.example.demo.controllers;

import java.util.Collections;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.example.demo.daos.AuthResponseDTO;
import com.example.demo.daos.LoginDTO;
import com.example.demo.daos.RegisterDTO;
import com.example.demo.daos.Role;
import com.example.demo.daos.UserEntity;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.JWTGenerator;

@RestController
@RequestMapping("/auth")
public class AuthController {
	

	private static final Logger log=LoggerFactory.getLogger(AuthController.class);
	
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
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO) {
	    log.info("register : START - Attempting to register a new user with username: {}", registerDTO.getUsername());

	    // Check if the username already exists
	    if (userRepository.existsByUsername(registerDTO.getUsername())) {
	        log.warn("register : Username '{}' already exists.", registerDTO.getUsername());
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                             .body("Username already exists.");
	    }

	    // Attempt to find the default 'USER' role
	    Optional<Role> userRole = roleRepository.findByName("USER");
	    if (!userRole.isPresent()) {
	        log.error("register : 'USER' role not found in database.");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("Role 'USER' not configured properly.");
	    }

	    // Create new UserEntity and set details
	    UserEntity user = new UserEntity();
	    user.setUsername(registerDTO.getUsername());
	    user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
	    user.setRoles(Collections.singletonList(userRole.get()));

	    // Save the new user
	    userRepository.save(user);
	    log.info("register : User '{}' registered successfully.", registerDTO.getUsername());

	    // Return response indicating successful registration
	    return ResponseEntity.status(HttpStatus.CREATED)
	                         .body("User registered successfully!");
	}


	
	
	@PostMapping("/login")
	public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO loginDTO) {
	    log.info("login : START - Attempting login for username: {}", loginDTO.getUsername());

	    try {
	        // Authenticate the user
	        Authentication authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
	        );

	        // Set authentication in security context
	        SecurityContextHolder.getContext().setAuthentication(authentication);

	        // Generate JWT token
	        String token = jwtGenerator.generateToken(authentication);
	        log.info("login : SUCCESS - User '{}' authenticated successfully.", loginDTO.getUsername());

	        // Return response with the token
	        return ResponseEntity.ok(new AuthResponseDTO(token));

	    } catch (Exception ex) {
	        log.warn("login : FAILED - Authentication failed for username: {}", loginDTO.getUsername());
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                             .body(new AuthResponseDTO("Authentication failed"));
	    }
	}


	
}
