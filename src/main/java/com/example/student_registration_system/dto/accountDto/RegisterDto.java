package com.example.student_registration_system.dto.accountDto;

import com.example.student_registration_system.entity.Role;
import lombok.Data;

@Data
public class RegisterDto {
    private String username;
    private String email;
    private String password;
    private Role role;
}
