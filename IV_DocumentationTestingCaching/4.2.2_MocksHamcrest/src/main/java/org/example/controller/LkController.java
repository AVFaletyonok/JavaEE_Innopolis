package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.student.StudentResponse;
import org.example.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/students/lk")
public class LkController {

    private final StudentService studentService;

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudent(@PathVariable("id") Long studentId) {
        Optional<StudentResponse> student = studentService.getStudent(studentId);
        return student.isPresent()
                ? ResponseEntity.of(student)
                : ResponseEntity.notFound().build();
    }
}
