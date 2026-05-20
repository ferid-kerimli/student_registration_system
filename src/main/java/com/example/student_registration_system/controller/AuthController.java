package com.example.student_registration_system.controller;

import com.example.student_registration_system.dto.accountDto.LoginDto;
import com.example.student_registration_system.dto.accountDto.RegisterDto;
import com.example.student_registration_system.response.ApiResponse;
import com.example.student_registration_system.response.JwtTokenResponse;
import com.example.student_registration_system.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtTokenResponse>> login(@RequestBody LoginDto dto) {
        ApiResponse<JwtTokenResponse> response = accountService.login(dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterDto>> register(@RequestBody RegisterDto dto) {
        ApiResponse<RegisterDto> response = accountService.register(dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Boolean>> logout(HttpServletRequest request) {
        ApiResponse<Boolean> response = accountService.logout(request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}