package com.ramosprodev.sql_application.repository;

import com.ramosprodev.sql_application.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByUsername(String username);
    Optional<UserEntity> findByUsername(String username);
    boolean existsByEmail(String email);
}