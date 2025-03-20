package org.example.service;

import org.example.model.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    Optional<Course> getCourseByName(final String courseName);

    boolean containsCourse(final String courseName);

    boolean containsAllCourses(final List<Long> coursesIds);
}
