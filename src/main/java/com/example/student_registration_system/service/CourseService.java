package com.example.student_registration_system.service;

import com.example.student_registration_system.dto.courseDto.CourseUpdateDto;
import com.example.student_registration_system.dto.courseDto.CreateCourseDto;
import com.example.student_registration_system.entity.Course;
import com.example.student_registration_system.entity.Role;
import com.example.student_registration_system.entity.User;
import com.example.student_registration_system.repository.CourseRepository;
import com.example.student_registration_system.repository.UserRepository;
import com.example.student_registration_system.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public ApiResponse<List<Course>> getAllCourses() {
        ApiResponse<List<Course>> response = new ApiResponse<>();

        try {
            List<Course> courses = courseRepository.findAll();

            response.success(courses, 200);
        } catch (Exception e) {
            response.failure(e.getMessage(), 500);
        }

        return response;
    }

    public ApiResponse<Course> getCourseById(Long id) {

        ApiResponse<Course> response = new ApiResponse<>();

        try {

            Course course = courseRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            response.success(course, 200);

        } catch (Exception e) {
            response.failure(e.getMessage(), 500);
        }

        return response;
    }

    public ApiResponse<Course> createCourse(CreateCourseDto dto) {

        ApiResponse<Course> response = new ApiResponse<>();

        try {

            String username = SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName();

            User professor = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (professor.getRole() != Role.PROFESSOR) {
                response.failure("Only professors can create courses", 403);
                return response;
            }

            if (courseRepository.existsByCourseCode(dto.getCourseCode())) {
                response.failure("Course code already exists", 400);
                return response;
            }

            Course course = Course.builder()
                    .courseName(dto.getCourseName())
                    .courseCode(dto.getCourseCode())
                    .credits(dto.getCredits())
                    .maxStudents(dto.getMaxStudents())
                    .professor(professor)
                    .build();

            courseRepository.save(course);

            response.success(course, 201);

        } catch (Exception e) {
            response.failure(e.getMessage(), 500);
        }

        return response;
    }

    public ApiResponse<Course> updateCourse(Long id, CourseUpdateDto dto) {

        ApiResponse<Course> response = new ApiResponse<>();

        try {

            String username = SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName();

            User professor = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Course course = courseRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            // only course owner can update
            if (!course.getProfessor().getId().equals(professor.getId())) {
                response.failure("You can only update your own courses", 403);
                return response;
            }

            course.setCourseName(dto.getCourseName());
            course.setCredits(dto.getCredits());
            course.setMaxStudents(dto.getMaxStudents());

            courseRepository.save(course);

            response.success(course, 200);

        } catch (Exception e) {
            response.failure(e.getMessage(), 500);
        }

        return response;
    }

    public ApiResponse<Boolean> deleteCourse(Long id) {

        ApiResponse<Boolean> response = new ApiResponse<>();

        try {

            String username = SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName();

            User professor = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Course course = courseRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            // only course owner can delete
            if (!course.getProfessor().getId().equals(professor.getId())) {
                response.failure("You can only delete your own courses", 403);
                return response;
            }

            courseRepository.delete(course);

            response.success(true, 200);

        } catch (Exception e) {
            response.failure(e.getMessage(), 500);
        }

        return response;
    }
}
