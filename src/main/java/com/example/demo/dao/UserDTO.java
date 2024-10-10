package com.example.demo.dao;

import java.sql.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;

public class UserDTO {
	
	private String username;

	private String role;
	
	private String email;
	
	private String firstname;
	
	private String lastname;
	
	private Date DOB;
	
	private String gender;
	
	private String title;
	
	private String externalId;
	
	private String createdAt;
	
	private String contactno;

	public UserDTO() {
	}

	

	public String getContactno() {
		return contactno;
	}



	public void setContactno(String contactno) {
		this.contactno = contactno;
	}



	public UserDTO(String username, String role, String email, String firstname, String lastname, Date dOB,
			String gender, String title, String externalId, String createdAt, String contactno) {
		super();
		this.username = username;
		this.role = role;
		this.email = email;
		this.firstname = firstname;
		this.lastname = lastname;
		DOB = dOB;
		this.gender = gender;
		this.title = title;
		this.externalId = externalId;
		this.createdAt = createdAt;
		this.contactno = contactno;
	}



	@Override
	public String toString() {
		return "UserDTO [username=" + username + ", role=" + role + ", email=" + email + ", firstname=" + firstname
				+ ", lastname=" + lastname + ", DOB=" + DOB + ", gender=" + gender + ", title=" + title
				+ ", externalId=" + externalId + ", createdAt=" + createdAt + ", contactno=" + contactno + "]";
	}



	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Date getDOB() {
		return DOB;
	}

	public void setDOB(Date dOB) {
		DOB = dOB;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	
	

}
