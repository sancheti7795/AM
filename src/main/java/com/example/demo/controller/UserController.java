package com.example.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dao.UserEntity;
import com.example.demo.dao.UserDTO;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
	
	private static final Logger log=LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	@GetMapping
	public ResponseEntity<List<UserDTO>> getAllUsers(){
		log.info("getAllUsers : START - Getting all users.");
		
		try {
			List<UserDTO> users=userService.getAllUsers();
			if(users.isEmpty()) {
				log.warn("getAllUsers : No users found.");
				return ResponseEntity.noContent().build();
			}
			log.info("getAllUsers : Successfully returning {} users.", users.size());
			return ResponseEntity.ok(users);
		}
		catch (Exception e) {
			log.error("getAllUsers :ERROR - An exception occurred while fetching users.", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		finally {
			log.info("getAllUsers : END - Getting all users.");
		}
		
	}
	
	@PostMapping("/saveUser")
	public ResponseEntity<UserDTO>  saveUser(@RequestBody @Valid UserEntity user) {
		log.info("saveUser : START - Save user with username - {} .",(user.getFirstname()+"."+user.getLastname()));
		
		try {
			
			UserDTO userDTO=userService.saveUser(user);
			if(userDTO==null) {
				log.warn("saveUser : Could not save user.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
			}
			log.info("saveUser : User saved successfully with username - {} .",(user.getFirstname()+"."+user.getLastname()));
			return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
		}
		catch (Exception e) {
			log.error("saveUser : ERROR - An exception occurred while saving user with username - {}. Exception: {}", (user.getFirstname()+"."+user.getLastname()), e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		finally {
			log.info("saveUser : END - Save user with username - {} .",(user.getFirstname()+"."+user.getLastname()));
		}
	}
	
	@PutMapping("/editUser/{id}")
	public ResponseEntity<String> editUser(@PathVariable int id, @RequestBody @Valid UserDTO userDTO){
		log.info("editUser : START - Edit user with id {} .",id);
		
		
		try {
			String response=userService.editUser(id,userDTO);
			if(response.isEmpty()) {
				log.warn("editUser : Could not edit user.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
			}
			log.info("editUser : User edited successfullly with id {} .",id);
			return ResponseEntity.status(HttpStatus.OK).body(response); 
			
		}
		catch (Exception e) {
			log.error("editUser : ERROR - An exception occurred while editing user with id - {}. Exception: {}", id, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		finally {
			log.info("editUser : END - Edit user with id {} .",id);
		}
		
	}
	
	
}
