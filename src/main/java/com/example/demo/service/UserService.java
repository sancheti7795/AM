package com.example.demo.service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.User;
import com.example.demo.dao.UserDTO;
import com.example.demo.repository.UserRepository;

import jakarta.validation.Valid;

@Service
public class UserService {
	
	private static final Logger log=LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserRepository userRepository;

	public List<UserDTO> getAllUsers() {
		log.info("getAllUsers : START - Fetching all users from database.");
		
		try {
			List<User> users=userRepository.findAll();
			List<UserDTO> usersDTO=convertUsersToUserDTOs(users);
			if(users.isEmpty()) {
				log.warn("getAllUsers : No users found in database.");
				return Collections.emptyList();
			}
			log.info("getAllUsers : Successfully fetched {} users from database.",users.size());
			return usersDTO;
		}
		catch(Exception e){
			log.error("getAllUsers : Error occurred while fetching users from database.", e);
			throw new RuntimeException("Error retrieving users.");
		}
		finally {
			log.info("getAllUsers : END - Fetching all users from database.");
		}
		
	}

	private List<UserDTO> convertUsersToUserDTOs(List<User> users) {
		List<UserDTO> usersDTO= new ArrayList<UserDTO>();
		for(User u : users) {
			UserDTO userDTO = new UserDTO();
			userDTO.setCreatedAt(u.getCreatedAt());
			userDTO.setDOB(u.getDOB());
			userDTO.setEmail(u.getEmail());
			userDTO.setExternalId(u.getExternalId());
			userDTO.setFirstname(u.getFirstname());
			userDTO.setLastname(u.getLastname());
			userDTO.setGender(u.getGender());
			userDTO.setRole(u.getRole());
			userDTO.setTitle(u.getTitle());
			userDTO.setUsername(u.getUsername());
			
			usersDTO.add(userDTO);
		}
		
		return usersDTO;
	}

	public UserDTO saveUser(User user) {
		log.info("saveUser : START - Saving user to database.");
		
		try {
			String username=generateUsername(user.getFirstname(),user.getLastname());
			String externalId=generateExternalId();
			user.setUsername(username);
			user.setExternalId(externalId);
			User userFromDB=userRepository.save(user);
			UserDTO userDTO = convertToUserDTO(userFromDB);
			if(userDTO==null) {
				log.warn("saveUser : Error occurred while saving user to database.");
			}
			log.info("saveUser : Successfully saved user with username {}.",userDTO.getUsername());
			return userDTO;
			
		}
			
		catch (Exception e) {
			log.error("saveUser : Error occurred while saving user to database.", e);
			throw new RuntimeException("Error saving user.");
		}
		
		finally {
			log.info("saveUser : END - Saving user to database.");
		}
	}

	private String generateExternalId() {
		
		String externalId;
		do {
			externalId=generateRandomId();
		}
		while(externalIdExists(externalId));
		return externalId;
	}

	 private String generateRandomId() {

		 String characters="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		 SecureRandom secureRandom=new SecureRandom();
		 StringBuilder idBuilder=new StringBuilder(6);
		 for(int i=0;i<6;i++) {
			 int index=secureRandom.nextInt(characters.length());
			 idBuilder.append(characters.charAt(index));
		 }
		return idBuilder.toString();
	}

	private boolean externalIdExists(String externalId) {
	        return userRepository.existsByExternalId(externalId);
	    }

	//Create username based on firstname & lastname
	private String generateUsername(String firstname, String lastname) {
		String baseUsername=(firstname+"."+lastname).toLowerCase();
		String username=baseUsername;
		int count=1;
		
		while(userRepository.existsByUsername(username)) {
			username=baseUsername+count;
			count++;
		}
		return username;
	}

	private UserDTO convertToUserDTO(User u) {
		UserDTO userDTO = new UserDTO();
		userDTO.setCreatedAt(u.getCreatedAt());
		userDTO.setDOB(u.getDOB());
		userDTO.setEmail(u.getEmail());
		userDTO.setExternalId(u.getExternalId());
		userDTO.setFirstname(u.getFirstname());
		userDTO.setLastname(u.getLastname());
		userDTO.setGender(u.getGender());
		userDTO.setRole(u.getRole());
		userDTO.setTitle(u.getTitle());
		userDTO.setUsername(u.getUsername());
		userDTO.setContactno(u.getContactno());
		return userDTO;
	}

	public String editUser(Long id, @Valid UserDTO userDTO) {
		
		Optional<User> existingUserInDB=userRepository.findById(id);
		if(!existingUserInDB.isPresent()) {
			return "No user found!";
		}
		
		User existingUser=existingUserInDB.get();
		
		//Update fields
		 	existingUser.setFirstname(userDTO.getFirstname());
	        existingUser.setLastname(userDTO.getLastname());
	        existingUser.setEmail(userDTO.getEmail());
	        existingUser.setDOB(userDTO.getDOB());
	        existingUser.setGender(userDTO.getGender());
	        existingUser.setRole(userDTO.getRole());
	        existingUser.setTitle(userDTO.getTitle());
	        existingUser.setContactno(userDTO.getContactno());
	        
	        userRepository.save(existingUser);
	        return "User edited successfully!";
	       
	}

}
