package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.Student;
import org.example.repository.StudentRepository;
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

    private final StudentService studentService;
    private final StudentRepository studentRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable("id") Long studentId) {
        if (studentId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Student> student = studentRepository.findById(studentId);
        return student.isPresent()
                ? ResponseEntity.of(student)
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student request) {
        if (studentService.isStudentCorrect(request)) {
            Student student = studentRepository.saveAndFlush(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(student);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(request);
    }

    @PutMapping
    public ResponseEntity<Optional<Student>> updateStudent(@RequestBody Student request) {
        if (request.getId() == null || request.getId() <= 0 ||
                !studentService.isStudentCorrect(request)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Optional.empty());
        }
        Optional<Student> studentOptional = studentRepository.findById(request.getId());
        if (studentOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Optional.of(request));
        }
        Student newStudent = studentRepository.saveAndFlush(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Optional.of(newStudent));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable("id") Long studentId) {
        if (studentId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isPresent()) {
            studentRepository.deleteById(studentId);
            return ResponseEntity.of(student);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Student>> getStudents() {
        List<Student> students = studentRepository.findAll();
        return ResponseEntity.ok(students);
    }

    @PostMapping("/{id}/courses/{course_name}")
    public ResponseEntity<Student> signUpForCourse(@PathVariable("id") Long studentId,
                                                   @PathVariable("course_name") String courseName) {
        if (studentId <= 0 || courseName == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Student> student = studentService.signUpCourse(studentId, courseName);
        return student.isPresent()
                ? ResponseEntity.of(student)
                : ResponseEntity.badRequest().build();
    }
}
