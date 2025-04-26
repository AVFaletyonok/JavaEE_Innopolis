package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.student.StudentResponse;
import org.example.entity.Student;
import org.example.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/students")
public class StudentController {

    //lombock doesn't work with Qualifier - should make manual constructor
//    @Qualifier(value = "studentService")
    private final StudentService studentService;

//    public StudentController(StudentService studentService) {
//        this.studentService = studentService;
//    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudent(@PathVariable("id") Long studentId) {
        Optional<StudentResponse> student = studentService.getStudent(studentId);
        return student.isPresent()
                ? ResponseEntity.of(student)
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@RequestBody Student request) {
        Optional<StudentResponse> student = studentService.createStudent(request);
        return student.isPresent()
                ? ResponseEntity.status(HttpStatus.CREATED).body(student.get())
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping
    public ResponseEntity<StudentResponse> updateStudent(@RequestBody Student request) {
        Optional<StudentResponse> student = studentService.updateStudent(request);
        return student.isPresent()
                ? ResponseEntity.status(HttpStatus.CREATED).body(student.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StudentResponse> deleteStudent(@PathVariable("id") Long studentId) {
        Optional<StudentResponse> student = studentService.deleteStudent(studentId);
        return student.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).body(student.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getStudents() {
        List<StudentResponse> students = studentService.getStudents();
        return ResponseEntity.ok(students);
    }

    @PostMapping("/{id}/courses/{course_name}")
    public ResponseEntity<StudentResponse> signUpCourse(@PathVariable("id") Long studentId,
                                                   @PathVariable("course_name") String courseName) {
        Optional<StudentResponse> student = studentService.signUpCourse(studentId, courseName);
        return student.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).body(student.get())
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
