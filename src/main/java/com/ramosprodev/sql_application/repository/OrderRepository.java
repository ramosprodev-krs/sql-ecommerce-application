package com.ramosprodev.sql_application.repository;

import com.ramosprodev.sql_application.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}