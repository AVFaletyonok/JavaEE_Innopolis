package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.aspect.LoggingAround;
import org.example.dto.student.StudentResponse;
import org.example.entity.StudentEntity;
import org.example.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
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

    @LoggingAround
    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@RequestBody StudentEntity request) {
        Optional<StudentResponse> student = studentService.createStudent(request);
        return student.isPresent()
                ? ResponseEntity.status(HttpStatus.CREATED).body(student.get())
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping
    public ResponseEntity<StudentResponse> updateStudent(@RequestBody StudentEntity request) {
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

    @LoggingAround
    @PostMapping("/{id}/courses/{course_name}")
    public ResponseEntity<StudentResponse> signUpCourse(@PathVariable("id") Long studentId,
                                                   @PathVariable("course_name") String courseName) {
        Optional<StudentResponse> student = studentService.signUpCourse(studentId, courseName);
        return student.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).body(student.get())
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @LoggingAround
    @GetMapping("/on-course={course_name}")
    public ResponseEntity<List<StudentResponse>> getStudentsByCourse(@PathVariable("course_name") String courseName) {
        Optional<List<StudentResponse>> students = studentService.getStudentsByCourse(courseName);
        return students.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).body(students.get())
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/older-than={age}")
    public ResponseEntity<List<StudentResponse>> getStudentsOlderThan(@PathVariable("age") Integer age) {
        Optional<List<StudentResponse>> students = studentService.getStudentsOlderThan(age);
        return students.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).body(students.get())
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
