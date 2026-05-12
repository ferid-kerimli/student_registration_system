package com.example.student_registration_system.controller;

import com.example.student_registration_system.dto.adminDto.UpdateUserRoleDto;
import com.example.student_registration_system.entity.Course;
import com.example.student_registration_system.entity.User;
import com.example.student_registration_system.response.ApiResponse;
import com.example.student_registration_system.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/getUsers")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        ApiResponse<List<User>> response = adminService.getAllUsers();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteUser(@PathVariable Long id) {
        ApiResponse<Boolean> response = adminService.deleteUser(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/updateUserRole/{id}/role")
    public ResponseEntity<ApiResponse<User>> updateRole(@PathVariable Long id, @RequestBody UpdateUserRoleDto dto) {
        ApiResponse<User> response = adminService.updateUserRole(id, dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/adjustStudentCountInCourse/{courseId}/max-students")
    public ResponseEntity<ApiResponse<Course>> adjustMaxStudents(@PathVariable Long courseId, @RequestParam Integer maxStudents) {
        ApiResponse<Course> response = adminService.adjustMaxStudentCount(courseId, maxStudents);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/courses/{courseId}/students/{studentId}")
    public ResponseEntity<ApiResponse<Boolean>> addStudent(@PathVariable Long courseId, @PathVariable Long studentId) {
        ApiResponse<Boolean> response = adminService.addStudentToCourse(studentId, courseId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/courses/{courseId}/students/{studentId}")
    public ResponseEntity<ApiResponse<Boolean>> removeStudent(@PathVariable Long courseId, @PathVariable Long studentId) {
        ApiResponse<Boolean> response = adminService.removeStudentFromCourse(studentId, courseId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}