package com.example.student_registration_system.service;

import com.example.student_registration_system.dto.jwtDto.TokenRequest;
import com.example.student_registration_system.entity.Role;
import com.example.student_registration_system.response.JwtTokenResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expire}")
    private int expireMinute;

    public JwtTokenResponse generateToken(TokenRequest tokenRequest, List<Role> roles) {
        try {

            SecretKey key = Keys.hmacShaKeyFor(
                    secret.getBytes(StandardCharsets.UTF_8)
            );

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expireDateTime = now.plusMinutes(expireMinute);

            Date issuedAt = Date.from(
                    now.atZone(ZoneId.systemDefault()).toInstant()
            );

            Date expireDate = Date.from(
                    expireDateTime.atZone(ZoneId.systemDefault()).toInstant()
            );

            String token = Jwts.builder()
                    .subject(tokenRequest.getUsername())
                    .claim("username", tokenRequest.getUsername())
                    .claim(
                            "roles",
                            roles.stream()
                                    .map(Role::name)
                                    .toList()
                    )
                    .issuedAt(issuedAt)
                    .expiration(expireDate)
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();

            return JwtTokenResponse.builder()
                    .token(token)
                    .expireDate(expireDateTime)
                    .build();

        } catch (Exception ex) {
            throw ex;
        }
    }
}