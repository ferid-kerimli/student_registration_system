package com.example.student_registration_system.service;

import com.example.student_registration_system.entity.*;
import com.example.student_registration_system.repository.CourseRepository;
import com.example.student_registration_system.repository.EnrollmentRepository;
import com.example.student_registration_system.repository.UserRepository;
import com.example.student_registration_system.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public ApiResponse<List<Enrollment>> getMyEnrollments() {

        ApiResponse<List<Enrollment>> response = new ApiResponse<>();

        try {

            String username = SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName();

            User student = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<Enrollment> enrollments =
                    enrollmentRepository.findByStudent(student);

            response.success(enrollments, 200);

        } catch (Exception e) {
            response.failure(e.getMessage(), 500);
        }

        return response;
    }

    public ApiResponse<Boolean> enroll(Long courseId) {

        ApiResponse<Boolean> response = new ApiResponse<>();

        try {

            String username = SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName();

            User student = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (student.getRole() != Role.STUDENT) {
                response.failure("Only students can enroll", 403);
                return response;
            }

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
                response.failure("Already enrolled", 400);
                return response;
            }

            long enrolledCount = enrollmentRepository.countByCourse(course);

            if (course.getMaxStudents() != null &&
                    enrolledCount >= course.getMaxStudents()) {

                response.failure("Course is full", 400);
                return response;
            }

            Enrollment enrollment = Enrollment.builder()
                    .student(student)
                    .course(course)
                    .build();

            enrollmentRepository.save(enrollment);

            response.success(true, 200);

        } catch (Exception e) {
            response.failure(e.getMessage(), 500);
        }

        return response;
    }

    public ApiResponse<Boolean> dropCourse(Long courseId) {

        ApiResponse<Boolean> response = new ApiResponse<>();

        try {

            String username = SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName();

            User student = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            Enrollment enrollment = enrollmentRepository
                    .findByStudentAndCourse(student, course)
                    .orElseThrow(() -> new RuntimeException("Enrollment not found"));

            enrollmentRepository.delete(enrollment);

            response.success(true, 200);

        } catch (Exception e) {
            response.failure(e.getMessage(), 500);
        }

        return response;
    }
}