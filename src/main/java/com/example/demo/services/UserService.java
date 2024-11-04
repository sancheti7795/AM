package com.example.demo.services;

import java.util.List;

import com.example.demo.daos.UserDTO;
import com.example.demo.daos.UserEntity;

public interface UserService {
	
	  public List<UserDTO> getAllUsers();
	  
	   public UserDTO saveUser(UserEntity user);
	   
	   public String editUser(int id,  UserDTO userDTO);
	   
}
