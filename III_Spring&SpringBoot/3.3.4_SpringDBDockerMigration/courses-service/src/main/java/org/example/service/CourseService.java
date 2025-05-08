package org.example.service;

import org.example.dto.CoursesResponse;
import org.example.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    Optional<CoursesResponse> getCourse(final Long courseId);

    Optional<CoursesResponse> createCourse(final Course course);

    Optional<CoursesResponse> updateCourse(final Course course);

    Optional<CoursesResponse> deleteCourse(final Long courseId);

    Optional<List<CoursesResponse>> getCourses();
}
