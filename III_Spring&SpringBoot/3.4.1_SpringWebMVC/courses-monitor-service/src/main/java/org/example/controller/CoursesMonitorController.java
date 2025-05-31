package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.course.CourseResponse;
import org.example.service.CourseMonitorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/monitor-courses")
public class CoursesMonitorController {

    private final CourseMonitorService courseMonitorService;

    @GetMapping("/courses/startedOnDay={day}")
    public ResponseEntity<List<CourseResponse>> getCoursesStartedOnDay(@PathVariable("day") String day) {
        // YYYY-MM-DD
        Optional<List<CourseResponse>> courses = courseMonitorService.getCoursesStartedOnDay(day);
        return courses.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).body(courses.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/courses/startedToday")
    public ResponseEntity<List<CourseResponse>> getCoursesStartedToday() {
        Optional<List<CourseResponse>> courses = courseMonitorService.getCoursesStartedToday();
        return courses.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).body(courses.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
