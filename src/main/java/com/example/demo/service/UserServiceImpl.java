package com.example.demo.service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dao.UserEntity;
import com.example.demo.dao.UserDTO;
import com.example.demo.repository.UserRepository;

import jakarta.validation.Valid;

@Service
public class UserServiceImpl implements UserService {
    
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserDTO> getAllUsers() {
        log.info("Fetching all users from database.");
        
        try {
            List<UserEntity> users = userRepository.findAll();
            System.out.println("===================================================================================");
            for(UserEntity e : users) {
            	System.out.println(e.getRoles());
            }
            List<UserDTO> usersDTO = convertUsersToUserDTOs(users);
            log.info("Successfully fetched {} users from database.", users.size());
            return usersDTO.isEmpty() ? Collections.emptyList() : usersDTO;
        } catch (Exception e) {
            log.error("Error occurred while fetching users from database.", e);
            throw new RuntimeException("Error retrieving users.");
        }
    }

    public List<UserDTO> convertUsersToUserDTOs(List<UserEntity> users) {
        log.info("Converting Users to UserDTOs");
        List<UserDTO> usersDTO = new ArrayList<>();
        for (UserEntity u : users) {
            usersDTO.add(convertToUserDTO(u));
        }
        return usersDTO;
    }

    public UserDTO saveUser(UserEntity user) {
        log.info("Saving user to database.");
        
        try {
            String externalId = generateExternalId();
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setExternalId(externalId);
            user.setPassword(encodedPassword);
            UserEntity userFromDB = userRepository.save(user);
            UserDTO userDTO = convertToUserDTO(userFromDB);
            log.info("Successfully saved user with username {}.", userDTO.getUsername());
            return userDTO;
        } catch (Exception e) {
            log.error("Error occurred while saving user to database.", e);
            throw new RuntimeException("Error saving user.");
        }
    }

    private String generateExternalId() {
        log.info("Generating externalId for user.");
        String externalId;
        do {
            externalId = generateRandomId();
        } while (externalIdExists(externalId));
        return externalId;
    }

    private String generateRandomId() {
        log.info("Generate a random ID for externalId.");
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder idBuilder = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            int index = secureRandom.nextInt(characters.length());
            idBuilder.append(characters.charAt(index));
        }
        return idBuilder.toString();
    }

    private boolean externalIdExists(String externalId) {
        return userRepository.existsByExternalId(externalId);
    }

    private UserDTO convertToUserDTO(UserEntity u) {
        log.info("Converting single User to UserDTO.");
        UserDTO userDTO = new UserDTO();
        userDTO.setCreatedAt(u.getCreatedAt());
        userDTO.setDOB(u.getDOB());
        userDTO.setEmail(u.getEmail());
        userDTO.setExternalId(u.getExternalId());
        userDTO.setFirstname(u.getFirstname());
        userDTO.setLastname(u.getLastname());
        userDTO.setGender(u.getGender());
        userDTO.setRoles(u.getRoles());
        userDTO.setUsername(u.getUsername());
        userDTO.setContactno(u.getContactno());
        return userDTO;
    }

    public String editUser(int id, @Valid UserDTO userDTO) {
        log.info("Editing user with id - {}.", id);
        
        try {
            UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No user found!"));
            
            existingUser.setFirstname(userDTO.getFirstname());
            existingUser.setLastname(userDTO.getLastname());
            existingUser.setEmail(userDTO.getEmail());
            existingUser.setDOB(userDTO.getDOB());
            existingUser.setGender(userDTO.getGender());
            existingUser.setRoles(userDTO.getRoles());
            existingUser.setTitle(userDTO.getTitle());
            existingUser.setContactno(userDTO.getContactno());
            existingUser.setUsername(userDTO.getUsername());
            
            userRepository.save(existingUser);
            return "User edited successfully!";
        } catch (Exception e) {
            log.error("Error while editing user with id - {}.", id, e);
            return "An error occurred while editing the user.";
        }
    }
}
