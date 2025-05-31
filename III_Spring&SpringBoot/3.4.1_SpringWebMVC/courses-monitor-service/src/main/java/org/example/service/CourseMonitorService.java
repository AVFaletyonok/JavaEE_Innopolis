package org.example.service;


import org.example.dto.course.CourseResponse;

import java.util.List;
import java.util.Optional;

public interface CourseMonitorService {

    void checkCourses();

    Optional<List<CourseResponse>> getCoursesStartedOnDay(final String day);

    Optional<List<CourseResponse>> getCoursesStartedToday();
}
