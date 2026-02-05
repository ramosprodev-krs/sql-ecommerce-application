package com.ramosprodev.sql_application.repository;

import com.ramosprodev.sql_application.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}