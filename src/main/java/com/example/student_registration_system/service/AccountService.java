package com.example.student_registration_system.service;

import com.example.student_registration_system.dto.accountDto.LoginDto;
import com.example.student_registration_system.dto.accountDto.RegisterDto;
import com.example.student_registration_system.dto.jwtDto.TokenRequest;
import com.example.student_registration_system.entity.User;
import com.example.student_registration_system.repository.UserRepository;
import com.example.student_registration_system.response.ApiResponse;
import com.example.student_registration_system.response.JwtTokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final BlacklistService blacklistService;

    public ApiResponse<JwtTokenResponse> login(LoginDto dto) {
        ApiResponse<JwtTokenResponse> response = new ApiResponse<>();

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.getUsername(),
                            dto.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) auth.getPrincipal();

            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            JwtTokenResponse token = tokenService.generateToken(
                    new TokenRequest(user.getUsername()),
                    List.of(user.getRole())
            );

            response.success(token, 200);
        } catch (Exception e) {
            response.failure("Invalid email or password", 401);
        }
        return response;
    }

    public ApiResponse<RegisterDto> register(RegisterDto dto) {
        ApiResponse<RegisterDto> response = new ApiResponse<>();

        try {
            if (dto.getUsername().isBlank() || dto.getEmail().isBlank() || dto.getPassword().isBlank() || dto.getRole() == null) {
                response.failure("All fields are required", 400);
                return response;
            }

            if (userRepository.existsByUsername(dto.getUsername())) {
                response.failure("Username already exists", 400);
                return response;
            }

            if (userRepository.existsByEmail(dto.getEmail())) {
                response.failure("Email already exists", 400);
                return response;
            }

            User user = User.builder()
                    .username(dto.getUsername())
                    .email(dto.getEmail()).password(passwordEncoder.encode(dto.getPassword()))
                    .role(dto.getRole()).enabled(true).build();

            userRepository.save(user);

            response.success(dto, 201);
        } catch (Exception e) {
            response.failure("Registration failed", 500);
        }
        return response;
    }

    public ApiResponse<Boolean> logout(HttpServletRequest request) {
        ApiResponse<Boolean> response = new ApiResponse<>();

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.failure("Token not found", 400);
            return response;
        }

        try {

            String token = authHeader.substring(7);

            blacklistService.blacklistToken(token);

            response.success(true, 200);

        } catch (Exception e) {

            response.failure("Logout failed", 500);
        }

        return response;
    }
}