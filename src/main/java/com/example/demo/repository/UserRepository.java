package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.dao.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

	boolean existsByUsername(String username);

	boolean existsByExternalId(String externalId);

	Optional<UserEntity> findById(Long id);
	
	@Query("SELECT u FROM UserEntity u JOIN FETCH u.roles WHERE u.username = :username")
	Optional<UserEntity> findByUsername(@Param("username") String username);

}
