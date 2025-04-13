package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.CoursesResponse;
import org.example.entity.Course;
import org.example.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/courses")
public class CoursesController {

    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<CoursesResponse> createCourse(@RequestBody Course request) {
        Optional<CoursesResponse> course = courseService.createCourse(request);
        return course.isPresent()
                ? ResponseEntity.status(HttpStatus.CREATED).body(course.get())
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoursesResponse> getCourse(@PathVariable("id") Long courseId) {
        Optional<CoursesResponse> course = courseService.getCourse(courseId);
        return course.isPresent()
                ? ResponseEntity.ok(course.get())
                : ResponseEntity.notFound().build();
    }

    @PutMapping
    public ResponseEntity<CoursesResponse> updateCourse(@RequestBody Course request) {
        Optional<CoursesResponse> course = courseService.updateCourse(request);
        return course.isPresent()
                ? ResponseEntity.status(HttpStatus.CREATED).body(course.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CoursesResponse> deleteCourse(@PathVariable("id") Long courseId) {
        Optional<CoursesResponse> course = courseService.deleteCourse(courseId);
        return course.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).body(course.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping
    public ResponseEntity<List<CoursesResponse>> getCourses() {
        Optional<List<CoursesResponse>> coursesResponses = courseService.getCourses();
        return coursesResponses.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).body(coursesResponses.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
