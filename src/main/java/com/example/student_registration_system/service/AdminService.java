package com.example.student_registration_system.service;

import com.example.student_registration_system.dto.adminDto.UpdateUserRoleDto;
import com.example.student_registration_system.entity.*;
import com.example.student_registration_system.repository.*;
import com.example.student_registration_system.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    // 👤 GET ALL USERS
    public ApiResponse<List<User>> getAllUsers() {

        ApiResponse<List<User>> response = new ApiResponse<>();

        try {

            response.success(userRepository.findAll(), 200);

        } catch (Exception e) {
            response.failure(e.getMessage(), 500);
        }

        return response;
    }

    public ApiResponse<Boolean> deleteUser(Long userId) {

        ApiResponse<Boolean> response = new ApiResponse<>();

        try {

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            userRepository.delete(user);

            response.success(true, 200);

        } catch (Exception e) {
            response.failure(e.getMessage(), 500);
        }

        return response;
    }

    public ApiResponse<User> updateUserRole(Long userId, UpdateUserRoleDto dto) {

        ApiResponse<User> response = new ApiResponse<>();

        try {

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            user.setRole(dto.getRole());

            userRepository.save(user);

            response.success(user, 200);

        } catch (Exception e) {
            response.failure(e.getMessage(), 500);
        }

        return response;
    }

    public ApiResponse<Boolean> addStudentToCourse(Long studentId, Long courseId) {

        ApiResponse<Boolean> response = new ApiResponse<>();

        try {

            User student = userRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            if (student.getRole() != Role.STUDENT) {
                response.failure("User is not a student", 400);
                return response;
            }

            if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
                response.failure("Student already enrolled", 400);
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

    public ApiResponse<Boolean> removeStudentFromCourse(Long studentId, Long courseId) {

        ApiResponse<Boolean> response = new ApiResponse<>();

        try {

            User student = userRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

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

    public ApiResponse<Course> adjustMaxStudentCount(Long courseId, Integer maxStudents) {

        ApiResponse<Course> response = new ApiResponse<>();

        try {

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            long enrolledCount = enrollmentRepository.countByCourse(course);

            // prevent setting max below current enrolled students
            if (maxStudents < enrolledCount) {
                response.failure(
                        "Max student count cannot be lower than current enrollment count",
                        400
                );
                return response;
            }

            course.setMaxStudents(maxStudents);

            courseRepository.save(course);

            response.success(course, 200);

        } catch (Exception e) {
            response.failure(e.getMessage(), 500);
        }

        return response;
    }
}