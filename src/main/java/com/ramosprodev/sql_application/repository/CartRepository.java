package com.ramosprodev.sql_application.repository;

import com.ramosprodev.sql_application.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<CartEntity, Long> {
}