package com.example.student_registration_system.controller;

import com.example.student_registration_system.dto.courseDto.CourseUpdateDto;
import com.example.student_registration_system.dto.courseDto.CreateCourseDto;
import com.example.student_registration_system.entity.Course;
import com.example.student_registration_system.response.ApiResponse;
import com.example.student_registration_system.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<List<Course>>> getAllCourses() {
        ApiResponse<List<Course>> response = courseService.getAllCourses();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse<Course>> getCourseById(@PathVariable Long id) {
        ApiResponse<Course> response = courseService.getCourseById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Course>> createCourse(@RequestBody CreateCourseDto dto) {
        ApiResponse<Course> response = courseService.createCourse(dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Course>> updateCourse(@PathVariable Long id, @RequestBody CourseUpdateDto dto) {
        ApiResponse<Course> response = courseService.updateCourse(id, dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteCourse(@PathVariable Long id) {
        ApiResponse<Boolean> response = courseService.deleteCourse(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
