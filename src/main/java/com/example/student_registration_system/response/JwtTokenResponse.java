package com.example.student_registration_system.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtTokenResponse {
    private String token;
    private LocalDateTime expireDate;
}
