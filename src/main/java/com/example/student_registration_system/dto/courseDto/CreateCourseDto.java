package com.example.student_registration_system.dto.courseDto;

import lombok.Data;

@Data
public class CreateCourseDto {
    private String courseName;

    private String courseCode;

    private Integer credits;

    private Integer maxStudents;
}
