package com.ramosprodev.sql_application.service;

import com.ramosprodev.sql_application.entity.CartEntity;
import com.ramosprodev.sql_application.entity.CartItemEntity;
import com.ramosprodev.sql_application.entity.UserEntity;
import com.ramosprodev.sql_application.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.NoSuchElementException;

@Service
public class PurchaseService {

    private final UserRepository userRepository;

    public PurchaseService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*
     * The purchase service class is responsible for managing users balance in different ways:
     * Deposit balance to a user
     * Calculate the outcome for a purchase and evaluate its possibility
     */


    // 1. Deposit balance
    @Transactional
    public UserEntity depositBalance(Long userId, BigDecimal balance) {
        var selectedUser = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found."));

        if (balance.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Balanced provided has to be higher than 0.");
        }

        selectedUser.setUserBalance(selectedUser.getUserBalance().add(balance));
        return userRepository.save(selectedUser);
    }

    // 2. Complete purchase
    public UserEntity completePurchase(Long userId) {
        var selectedUser = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found."));

        var selectedCart = selectedUser.getCart();

        if (selectedCart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty.");
        }

        var totalAmount = selectedCart.getCartItems().stream()
                .map(CartItemEntity::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (selectedUser.getUserBalance().compareTo(totalAmount) < 0) {
            throw new IllegalStateException("Insufficient balance to complete the purchase.");
        }


        selectedUser.setUserBalance(selectedUser.getUserBalance().subtract(totalAmount));
        selectedUser.setCart(new CartEntity());

        return userRepository.save(selectedUser);
    }

}
