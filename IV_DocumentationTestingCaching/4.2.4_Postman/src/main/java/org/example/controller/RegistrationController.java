package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.student.StudentResponse;
import org.example.entity.StudentEntity;
import org.example.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
// @Controller - to handle /WEB-INF/jsp/home.jsp in previous versions this app
@RequiredArgsConstructor
@RequestMapping("/api/v1/students")
public class RegistrationController {

    private final StudentService studentService;

    @PostMapping("/registration")
    public ResponseEntity<StudentResponse> addUser(@RequestBody StudentEntity request) {

        if (!request.getPassword().equals(request.getPasswordConfirm())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Optional<StudentResponse> newStudent = studentService.createStudent(request);
        return newStudent.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).body(newStudent.get())
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
