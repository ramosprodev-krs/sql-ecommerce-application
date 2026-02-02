package com.ramosprodev.sql_application.service;

import com.ramosprodev.sql_application.configuration.SecurityConfiguration;
import com.ramosprodev.sql_application.dto.UserDTO;
import com.ramosprodev.sql_application.entity.CartEntity;
import com.ramosprodev.sql_application.entity.UserEntity;
import com.ramosprodev.sql_application.entity.UserRole;
import com.ramosprodev.sql_application.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SecurityConfiguration securityConfiguration;

    public UserService(UserRepository userRepository, SecurityConfiguration securityConfiguration) {
        this.userRepository = userRepository;
        this.securityConfiguration = securityConfiguration;
    }

    // CRUD Methods:

    // Create
    public UserEntity createUser(UserDTO userDTO) {
        var encodedPassword = securityConfiguration.passwordEncoder().encode(userDTO.getPassword());
        var currentTime = LocalDateTime.now();

        UserEntity user = new UserEntity();
        user.setUsername(userDTO.getUsername());
        user.setPassword(encodedPassword);
        user.setEmail(userDTO.getEmail());
        user.setUserBalance(BigDecimal.valueOf(0.0));
        user.setCart(new CartEntity());
        user.setUserRole(UserRole.USER);
        user.setCreatedAt(currentTime);

        userRepository.save(user);
        return user;
    }

    // Read (single user)
    public Optional<UserEntity> readUserById(Long id) {
        return userRepository.findById(id);
    }

    // Read (all users)
    public List<UserEntity> readAllUsers() {
        return userRepository.findAll();
    }
}
