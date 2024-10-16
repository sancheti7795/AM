package com.example.demo.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name ="user")
@Getter
@Setter
public class UserEntity {
	
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
    private int id;
	
	private String username;
	
	private String contactno;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name="user_roles", joinColumns = @JoinColumn(name="user_id", referencedColumnName = "id"),
	inverseJoinColumns = @JoinColumn(name="role_id", referencedColumnName = "id"))
	private List<Role> roles=new ArrayList<>();

	private String email;
	
	private String firstname;
	
	private String lastname;
	
	private String password;
	
	private Date DOB;
	
	private String gender;
	
	private String title;
	
	private String externalId;
	
	@CreationTimestamp
	private String createdAt;
	
	private int isactive;

	@Override
	public String toString() {
		return "UserEntity [id=" + id + ", username=" + username + ", contactno=" + contactno + ", roles=" + roles
				+ ", email=" + email + ", firstname=" + firstname + ", lastname=" + lastname + ", password=" + password
				+ ", DOB=" + DOB + ", gender=" + gender + ", title=" + title + ", externalId=" + externalId
				+ ", createdAt=" + createdAt + ", isactive=" + isactive + "]";
	}

	public UserEntity() {
	
	}

	public UserEntity(int id, String username, String contactno, List<Role> roles,
			@Email(message = "Email should be valid") @NotBlank(message = "Email is mandatory") String email,
			@NotBlank(message = "First name is mandatory") String firstname,
			@NotBlank(message = "Last name is mandatory") String lastname,
			@NotBlank(message = "Password is mandatory") String password, Date dOB, String gender, String title,
			String externalId, String createdAt, int isactive) {
		super();
		this.id = id;
		this.username = username;
		this.contactno = contactno;
		this.roles = roles;
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

	public String getContactno() {
		return contactno;
	}

	public void setContactno(String contactno) {
		this.contactno = contactno;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
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

	public int getIsactive() {
		return isactive;
	}

	public void setIsactive(int isactive) {
		this.isactive = isactive;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	
}
