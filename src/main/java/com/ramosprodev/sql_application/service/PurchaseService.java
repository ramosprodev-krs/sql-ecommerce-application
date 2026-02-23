package com.ramosprodev.sql_application.service;

import com.ramosprodev.sql_application.entity.*;
import com.ramosprodev.sql_application.repository.OrderRepository;
import com.ramosprodev.sql_application.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PurchaseService {

    private final UserRepository userRepository;
    private final OrderRepository orderEntityRepository;

    public PurchaseService(UserRepository userRepository,
                           OrderRepository orderEntityRepository) {
        this.userRepository = userRepository;
        this.orderEntityRepository = orderEntityRepository;
    }

    /**
     * The PurchaseService class manages mainly 4 different things, those being:
     * 1. Depositing balance to a user
     * 2. Completing a purchase
     * 3. Reading user orders
     * 4. Displaying user's balance
     **/

    // 1. Deposit balance
    @Transactional
    public UserEntity depositBalance(Long userId, BigDecimal balance) {
        // Selecting the user
        var selectedUser = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found."));

        // Validating provided balance
        if (balance.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Balanced provided has to be higher than 0.");
        }

        // Adding balance to the user
        selectedUser.setUserBalance(selectedUser.getUserBalance().add(balance));

        return userRepository.save(selectedUser);
    }

    // 2. Complete purchase
    public OrderEntity completePurchase() {
        // Selecting the user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity selectedUser =  userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found."));

        // Selecting the user cart
        var selectedCart = selectedUser.getCart();

        // Verifying cart emptiness
        if (selectedCart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty.");
        }

        // Getting the total amount based on each total price
        var totalAmount = selectedCart.getCartPrice();

        // Verifying user balance
        if (selectedUser.getUserBalance().compareTo(totalAmount) < 0) {
            throw new IllegalStateException("Insufficient balance to complete the purchase.");
        }

        // Creating the purchase order
        OrderEntity order = new OrderEntity();
        LocalDateTime currentTime = LocalDateTime.now();
        order.setPurchasedAt(currentTime);
        order.setUser(selectedUser);
        order.setPurchaseTotalPrice(totalAmount);

        // Converting cart items into order items
        var orderItems = selectedCart.getCartItems().stream()
                        .map(cartItem -> {

                            OrderItemEntity orderItem = new OrderItemEntity();
                            orderItem.setName(cartItem.getProduct().getName());
                            orderItem.setQuantity(cartItem.getQuantity());
                            orderItem.setCurrentPrice(cartItem.getUnitPrice());
                            orderItem.setTotalPrice(cartItem.getTotalPrice());
                            orderItem.setOrder(order);
                            return orderItem;
                        }).toList();

        // Decreasing user balance based on the total amount
        selectedUser.setUserBalance(selectedUser.getUserBalance().subtract(totalAmount));

        // Setting order list
        order.setItemsList(orderItems);

        // Emptying the user cart
        selectedCart.getCartItems().clear();

        // Saving the user
        userRepository.save(selectedUser);

        // Saving the order
        return orderEntityRepository.save(order);
    }

    // 3. Read orders
    public List<OrderEntity> readOrders() {
        // Selecting the user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity selectedUser =  userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found."));

        return selectedUser.getOrdersList();
    }

    // 4. Get my balance
    public BigDecimal getMyBalance() {
        // Selecting the user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity selectedUser =  userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found."));

        return selectedUser.getUserBalance();
    }

}
