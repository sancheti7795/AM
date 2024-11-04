package com.example.demo.services;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.daos.UserEntity;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.exceptions.UserSaveException;
import com.example.demo.daos.UserDTO;
import com.example.demo.repositories.UserRepository;

import jakarta.validation.Valid;

@Service
public class UserServiceImpl implements UserService {
    
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserDTO> getAllUsers() {
        log.info("getAllUsers : START - Fetching all users from the database.");

        try {
            // Fetch all users and log roles (if needed)
            List<UserEntity> users = userRepository.findAll();
            users.forEach(user -> log.debug("User '{}' has roles: {}", user.getUsername(), user.getRoles()));

            // Convert to DTOs
            List<UserDTO> usersDTO = convertUsersToUserDTOs(users);
            log.info("getAllUsers : SUCCESS - Retrieved {} users from the database.", users.size());

            return usersDTO;
        } catch (Exception ex) {
            log.error("getAllUsers : ERROR - Error occurred while fetching users.", ex);
            throw new RuntimeException("An error occurred while retrieving users.");
        }
    }


    public List<UserDTO> convertUsersToUserDTOs(List<UserEntity> users) {
        log.info("Converting {} users to UserDTOs.", users.size());

        return users.stream()
                    .map(this::convertToUserDTO)
                    .collect(Collectors.toList());
    }


    public UserDTO saveUser(UserEntity user) {
        log.info("saveUser : START - Saving user with username: {}", user.getUsername());

        try {
            // Encode password only if not already encoded
            if (!isPasswordEncoded(user.getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            // Save user to the database
            UserEntity savedUser = userRepository.save(user);
            log.info("saveUser : SUCCESS - User '{}' saved to database.", user.getUsername());

            // Convert to DTO and return
            return convertToUserDTO(savedUser);

        } catch (Exception ex) {
            log.error("saveUser : ERROR - Failed to save user '{}'", user.getUsername(), ex);
            throw new UserSaveException("An error occurred while saving the user.", ex);
        }
    }
    
    private boolean isPasswordEncoded(String password) {
        return password != null && password.startsWith("{bcrypt}");
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
        log.info("generateRandomId : START - Generate a random ID for externalId.");
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder idBuilder = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            int index = secureRandom.nextInt(characters.length());
            idBuilder.append(characters.charAt(index));
        }
        log.info("generateRandomId : END - Generated a random ID for externalId.");
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
        log.info("editUser : START - Editing user with ID: {}", id);

        try {
            // Find the existing user or throw a custom exception if not found
            UserEntity existingUser = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));

            // Update fields from DTO
            updateUserFields(existingUser, userDTO);
            userRepository.save(existingUser);

            log.info("editUser : SUCCESS - User with ID '{}' edited successfully.", id);
            return "User edited successfully!";

        } catch (UserNotFoundException ex) {
            log.warn("editUser : FAILED - {}", ex.getMessage());
            return "User not found!";

        } catch (Exception ex) {
            log.error("editUser : ERROR - Error while editing user with ID: {}", id, ex);
            return "An error occurred while editing the user.";
        }
    }

    // Helper method to update user fields from DTO
    private void updateUserFields(UserEntity user, UserDTO userDTO) {
        user.setFirstname(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setEmail(userDTO.getEmail());
        user.setDOB(userDTO.getDOB());
        user.setGender(userDTO.getGender());
        user.setRoles(userDTO.getRoles());
        user.setTitle(userDTO.getTitle());
        user.setContactno(userDTO.getContactno());
        user.setUsername(userDTO.getUsername());
    }

}
