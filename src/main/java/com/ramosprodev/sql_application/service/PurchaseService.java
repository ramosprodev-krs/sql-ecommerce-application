package com.ramosprodev.sql_application.service;

import com.ramosprodev.sql_application.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PurchaseService {

    private final UserRepository userRepository;

    public PurchaseService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*
     * The purchase service class is responsible for managing users balance in different ways:
     * Add "money" to a user balance
     * Calculate the outcome for a purchase and evaluate its possibility
     */

}
