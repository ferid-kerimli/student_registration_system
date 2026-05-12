package com.example.student_registration_system.dto.adminDto;

import com.example.student_registration_system.entity.Role;
import lombok.Data;

@Data
public class UpdateUserRoleDto {
    private Role role;
}
