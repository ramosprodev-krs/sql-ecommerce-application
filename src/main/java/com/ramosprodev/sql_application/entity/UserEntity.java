package com.ramosprodev.sql_application.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name = "username")
    @NotBlank(message = "The provided username can't be blank.")
    @Size(min = 5, max = 20)
    private String username;

    @Column(name = "password")
    @NotBlank(message = "The provided password can't be blank.")
    @Size(min = 5)
    private String password;

    @Column(name = "email")
    @Email(message = "Please provide a valid E-mail.")
    private String email;

    @Column(name = "user_balance")
    private BigDecimal userBalance;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id")
    @JsonIgnore
    private CartEntity cart;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<OrderEntity> ordersList = new ArrayList<>();

    @OneToMany(mappedBy = "creator")
    @JsonIgnore
    private List<ProductEntity> productsList = new ArrayList<>();

    @JsonIgnore
    private Set<UserRole> userRoles = new HashSet<>();

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userRoles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole()))
                .toList();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
