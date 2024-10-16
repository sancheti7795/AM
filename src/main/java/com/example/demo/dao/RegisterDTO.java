package com.example.demo.dao;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class RegisterDTO {
	
	private String username;
	private String password;
	
	public RegisterDTO(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public RegisterDTO() {
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
