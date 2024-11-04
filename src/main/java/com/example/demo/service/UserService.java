package com.example.demo.service;

import java.util.List;

import com.example.demo.dao.UserDTO;
import com.example.demo.dao.UserEntity;

public interface UserService {
	
	  public List<UserDTO> getAllUsers();
	  
	   public UserDTO saveUser(UserEntity user);
	   
	   public String editUser(int id,  UserDTO userDTO);
	   
}
