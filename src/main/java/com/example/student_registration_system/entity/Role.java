package com.example.student_registration_system.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    ADMIN,
    STUDENT,
    PROFESSOR;

    @JsonCreator
    public static Role from(String value) {
        return Role.valueOf(value.trim().toUpperCase());
    }
}
