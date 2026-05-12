package com.example.student_registration_system.dto.courseDto;

import lombok.Data;

@Data
public class CourseUpdateDto {
    private String courseName;

    private Integer credits;

    private Integer maxStudents;
}
