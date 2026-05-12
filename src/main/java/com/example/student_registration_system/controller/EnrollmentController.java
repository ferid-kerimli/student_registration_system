package com.example.student_registration_system.controller;

import com.example.student_registration_system.entity.Enrollment;
import com.example.student_registration_system.response.ApiResponse;
import com.example.student_registration_system.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<List<Enrollment>>> getMyEnrollments() {
        ApiResponse<List<Enrollment>> response = enrollmentService.getMyEnrollments();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/enroll")
    public ResponseEntity<ApiResponse<Boolean>> enroll(Long courseId) {
        ApiResponse<Boolean> response = enrollmentService.enroll(courseId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/drop")
    public ResponseEntity<ApiResponse<Boolean>> dropCourse(Long courseId) {
        ApiResponse<Boolean> response = enrollmentService.dropCourse(courseId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
