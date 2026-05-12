package com.example.student_registration_system.repository;

import com.example.student_registration_system.entity.Course;
import com.example.student_registration_system.entity.Enrollment;
import com.example.student_registration_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudent(User student);
    boolean existsByStudentAndCourse(User student, Course course);
    long countByCourse(Course course);
    Optional<Enrollment> findByStudentAndCourse(User student, Course course);
}
