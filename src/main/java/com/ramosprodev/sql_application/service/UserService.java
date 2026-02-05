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
import java.util.NoSuchElementException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SecurityConfiguration securityConfiguration;

    public UserService(UserRepository userRepository, SecurityConfiguration securityConfiguration) {
        this.userRepository = userRepository;
        this.securityConfiguration = securityConfiguration;
    }

    /*
     * The following methods compose the CRUD methods, in this case for the user management.
     * This class also implements the use of encoding for passwords.
     */

    // CRUD Methods:

    // 1. Create user
    public UserEntity createUser(UserDTO userDTO) {
        if (userDTO == null) {
            throw new IllegalArgumentException("User DTO can't be null.");
        }
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

    // 2. Read all users
    public List<UserEntity> readAllUsers() {
        return userRepository.findAll();
    }

    // 2.1 Read a single user
    public UserEntity readUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found."));
    }

    // 3. Update user
    public UserEntity updateUserById(Long id, UserDTO userDTO) {
        UserEntity selectedUser = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found."));

        if (userDTO.getEmail() != null) {
            selectedUser.setEmail(userDTO.getEmail());
        }

        if (userDTO.getUsername() != null) {
            selectedUser.setUsername(userDTO.getUsername());
        }

        if (userDTO.getPassword() != null) {
            var encodedPassword = securityConfiguration.passwordEncoder().encode(userDTO.getPassword());
            selectedUser.setPassword(encodedPassword);
        }

        return userRepository.save(selectedUser);
    }

    // 4. Delete user
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)){
            throw new NoSuchElementException("User not found.");
        }

        userRepository.deleteById(id);
    }

}
