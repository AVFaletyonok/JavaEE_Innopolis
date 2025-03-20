package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.Student;
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
    public ResponseEntity<Student> getStudent(@PathVariable("id") Long studentId) {
        Optional<Student> student = studentService.getStudent(studentId);
        return student.isPresent()
                ? ResponseEntity.of(student)
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student request) {
        Optional<Student> student = studentService.createStudent(request);
        return student.isPresent()
                ? ResponseEntity.status(HttpStatus.CREATED).body(student.get())
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(request);
    }

    @PutMapping
    public ResponseEntity<Student> updateStudent(@RequestBody Student request) {
        Optional<Student> student = studentService.updateStudent(request);
        return student.isPresent()
                ? ResponseEntity.status(HttpStatus.CREATED).body(student.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable("id") Long studentId) {
        Optional<Student> student = studentService.deleteStudent(studentId);
        return student.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).body(student.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping
    public ResponseEntity<List<Student>> getStudents() {
        List<Student> students = studentService.getStudents();
        return ResponseEntity.ok(students);
    }

    @PostMapping("/{id}/courses/{course_name}")
    public ResponseEntity<Student> signUpForCourse(@PathVariable("id") Long studentId,
                                                   @PathVariable("course_name") String courseName) {
        Optional<Student> student = studentService.signUpCourse(studentId, courseName);
        return student.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).body(student.get())
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
