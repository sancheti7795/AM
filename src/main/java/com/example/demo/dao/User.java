package com.example.demo.dao;

import java.sql.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name ="user")
public class User {
	
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
    private int id;
	
	private String username;
	
	private String contactno;

	@NotBlank(message = "Role is mandatory")
	private String role;
	
	@Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
	private String email;
	
	@NotBlank(message = "First name is mandatory")
	private String firstname;
	
	@NotBlank(message = "Last name is mandatory")
	private String lastname;
	
	@NotBlank(message = "Password is mandatory")
	private String password;
	
	private Date DOB;
	
	private String gender;
	
	private String title;
	
	@Column(name = "externalid")
	private String externalId;
	
	@CreationTimestamp
	private String createdAt;
	
	private int isactive;

	public User() {
		
	}
	
	

	public int isIsactive() {
		return isactive;
	}


	public void setIsactive(int isactive) {
		this.isactive = isactive;
	}



	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", contactno=" + contactno + ", role=" + role + ", email="
				+ email + ", firstname=" + firstname + ", lastname=" + lastname + ", password=" + password + ", DOB="
				+ DOB + ", gender=" + gender + ", title=" + title + ", externalId=" + externalId + ", createdAt="
				+ createdAt + ", isactive=" + isactive + "]";
	}






	public User(int id, String username, String contactno, @NotBlank(message = "Role is mandatory") String role,
			@Email(message = "Email should be valid") @NotBlank(message = "Email is mandatory") String email,
			@NotBlank(message = "First name is mandatory") String firstname,
			@NotBlank(message = "Last name is mandatory") String lastname,
			@NotBlank(message = "Password is mandatory") String password, Date dOB, String gender, String title,
			String externalId, String createdAt, int isactive) {
		super();
		this.id = id;
		this.username = username;
		this.contactno = contactno;
		this.role = role;
		this.email = email;
		this.firstname = firstname;
		this.lastname = lastname;
		this.password = password;
		DOB = dOB;
		this.gender = gender;
		this.title = title;
		this.externalId = externalId;
		this.createdAt = createdAt;
		this.isactive = isactive;
	}






	public String getContactno() {
		return contactno;
	}





	public void setContactno(String contactno) {
		this.contactno = contactno;
	}





	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
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



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
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


}
