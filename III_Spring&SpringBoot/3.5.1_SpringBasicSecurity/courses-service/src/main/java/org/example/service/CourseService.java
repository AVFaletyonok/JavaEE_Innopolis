package org.example.service;

import org.example.dto.CourseResponse;
import org.example.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    Optional<CourseResponse> getCourse(final Long courseId);

    Optional<CourseResponse> createCourse(final Course course);

    Optional<CourseResponse> updateCourse(final Course course);

    Optional<CourseResponse> deleteCourse(final Long courseId);

    Optional<List<CourseResponse>> getCourses();
}
