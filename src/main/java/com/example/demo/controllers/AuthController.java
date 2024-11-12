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
import com.example.demo.daos.ResetPasswordRequest;
import com.example.demo.daos.Role;
import com.example.demo.daos.UserEntity;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.JWTGenerator;
import com.example.demo.services.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	
	@Autowired
	private AuthService authService;
	
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
	    finally {
	    	 log.info("login : END - Attempting login for username: {}", loginDTO.getUsername());
		}
	}
	
	
	@PostMapping("/resetPassword")
	public ResponseEntity<String>  resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
		
		 log.info("resetPassword : START - Attempting to reset password for username: {}", resetPasswordRequest.getLoginDTO().getUsername());
		 
		 System.out.println();
		 try {
			 String response=authService.resetPassword(resetPasswordRequest);
			 log.info("resetPassword : SUCCESS - Reset password successful for User '{}' .",  resetPasswordRequest.getLoginDTO().getUsername());
			 return ResponseEntity.status(HttpStatus.OK).body(response);
		 }
		 catch (Exception e) {
			 log.warn("login : FAILED - Authentication failed for username: {}",  resetPasswordRequest.getLoginDTO().getUsername());
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with ID " +  resetPasswordRequest.getLoginDTO().getUsername()+ " not found");
		}
		 finally {
			 log.info("resetPassword : END - Attempt to reset password for username: {}",  resetPasswordRequest.getLoginDTO().getUsername());
		}
		 
	}
	
	@PostMapping("/generateOTP")
	public ResponseEntity<String> generateOTP(@RequestBody String username1 )  {
		
		log.info("generateOTP : START - Generating OTP for resetting password for username: {}", username1);
		
		ObjectMapper objectMapper = new ObjectMapper();

        // Parse JSON string to JsonNode
        JsonNode jsonNode = null;
		try {
			jsonNode = objectMapper.readTree(username1);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // Extract the "username" field
        String username = jsonNode.get("username1").asText();

		
		if (!userRepository.existsByUsername(username)) {
	        log.warn("generateOTP : Username '{}' does not exist.",username);
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                             .body("Username does not exist.");
	    }
		 
		 try {
			 String response=authService.generateOTP(username);
			 log.info("generateOTP : SUCCESS - Generated OTP successfully for User '{}' .", username);
			 return ResponseEntity.status(HttpStatus.OK).body(response);
		 }
		 catch (Exception e) {
			 log.warn("generateOTP : FAILED - OTP Generation failed for username: {}", username);
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OTP Could not be generated for User with username " + username);
		} 
		 
		 
	}	
}
