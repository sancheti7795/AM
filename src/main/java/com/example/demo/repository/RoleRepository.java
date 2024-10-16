package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.dao.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{
	
	Optional<Role> findByName(String name);

}
