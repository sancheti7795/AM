package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.dao.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

	boolean existsByUsername(String username);

	boolean existsByExternalId(String externalId);

	Optional<UserEntity> findById(Long id);
	
	Optional<UserEntity> findByUsername(String username);

}
