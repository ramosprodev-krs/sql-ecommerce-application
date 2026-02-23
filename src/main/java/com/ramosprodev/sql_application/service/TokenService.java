package com.ramosprodev.sql_application.service;

import com.ramosprodev.sql_application.entity.UserEntity;
import com.ramosprodev.sql_application.entity.UserRole;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Service
public class TokenService {

    /**
     * The TokenService class sets the token's properties for JWT.
     * The use of those methods guarantee the security of this application.
     **/

    /*
     * The two following attributes use values contained in the properties classes
     * Meaning they can vary depending on your specific token.
     */

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expirationTime;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }


    // 1. Token generation
    public String generateToken(UserEntity userEntity) {
        var roles = userEntity.getUserRoles().stream()
                .map(UserRole::getRole)
                .toList();

        return Jwts.builder()
                .setSubject(userEntity.getUsername())
                .claim("role", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 2. Token validation
    public String validateToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
