package com.ramosprodev.sql_application.service;

import com.ramosprodev.sql_application.dto.RegisterDTO;
import com.ramosprodev.sql_application.dto.UserDTO;
import com.ramosprodev.sql_application.entity.CartEntity;
import com.ramosprodev.sql_application.entity.ProductEntity;
import com.ramosprodev.sql_application.entity.UserEntity;
import com.ramosprodev.sql_application.entity.UserRole;
import com.ramosprodev.sql_application.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /*
     * The following methods compose the CRUD methods, in this case for the user management.
     * This class also implements the use of encoding for passwords.
     */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    // CRUD Methods:

    // 1. Create user
    @Transactional
    public UserEntity createUser(UserDTO userDTO) {
        if (userDTO == null) {
            throw new IllegalArgumentException("User DTO can't be null.");
        }

        if (userRepository.existsByUsername(userDTO.getUsername()) || userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("User already exists.");
        }

        var encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        var currentTime = LocalDateTime.now();

        UserEntity user = new UserEntity();
        user.setUsername(userDTO.getUsername());
        user.setPassword(encodedPassword);
        user.setEmail(userDTO.getEmail());
        user.setUserBalance(userDTO.getUserBalance());
        user.setCart(new CartEntity());
        user.getUserRoles().addAll(userDTO.getUserRoles());
        user.setCreatedAt(currentTime);

        userRepository.save(user);
        return user;
    }

    // 1.1 Register user
    @Transactional
    public UserEntity registerUser(RegisterDTO registerDTO) {
        if (registerDTO == null) {
            throw new IllegalArgumentException("Register DTO can't be null.");
        }

        if (userRepository.existsByUsername(registerDTO.getUsername()) || userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new IllegalArgumentException("User already exists.");
        }

        var encodedPassword = passwordEncoder.encode(registerDTO.getPassword());
        var currentTime = LocalDateTime.now();

        UserEntity user = new UserEntity();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(encodedPassword);
        user.setEmail(registerDTO.getEmail());
        user.setUserBalance(BigDecimal.valueOf(0.0));
        user.setCart(new CartEntity());
        user.getUserRoles().add(UserRole.USER);
        user.setCreatedAt(currentTime);

        userRepository.save(user);
        return user;
    }

    // 2. Read all users
    @Transactional
    public List<UserEntity> readAllUsers() {
        return userRepository.findAll();
    }

    // 2.1 Read a single user
    @Transactional
    public UserEntity readUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found."));
    }

    // 3. Update a single user
    @Transactional
    public UserEntity updateUserById(Long id, UserDTO userDTO) {
        UserEntity selectedUser = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found."));

        if (userDTO.getUsername() != null && !userDTO.getUsername().isBlank()) {
            selectedUser.setUsername(userDTO.getUsername());
        }

        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            var encodedPassword = passwordEncoder.encode(userDTO.getPassword());
            selectedUser.setPassword(encodedPassword);
        }

        if (userDTO.getEmail() != null && !userDTO.getEmail().isBlank()) {
            selectedUser.setEmail(userDTO.getEmail());
        }

        if (userDTO.getUserBalance() != null && userDTO.getUserBalance().compareTo(BigDecimal.ZERO) > 0) {
            selectedUser.setUserBalance(userDTO.getUserBalance());
        }

        if (userDTO.getUserRoles() != null && !userDTO.getUserRoles().isEmpty()) {
            selectedUser.getUserRoles().clear();
            selectedUser.getUserRoles().addAll(userDTO.getUserRoles());
        }

        return userRepository.save(selectedUser);
    }

    // 4. Delete a single user
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)){
            throw new NoSuchElementException("User not found.");
        }

        userRepository.deleteById(id);
    }

    // Alternative method

    // Promotion methods:

    // 1. Manager promotion
    public Set<UserRole> managerPromote(Long userId) {
        UserEntity selectedUser =  userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found."));

        var roles = selectedUser.getUserRoles();
        roles.add(UserRole.MANAGER);

        userRepository.save(selectedUser);
        return roles;
    }

    // 2. Admin promotion
    public Set<UserRole> adminPromote(Long userId) {
        UserEntity selectedUser =  userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found."));

        var roles = selectedUser.getUserRoles();
        roles.add(UserRole.ADMIN);

        userRepository.save(selectedUser);
        return roles;
    }

}
