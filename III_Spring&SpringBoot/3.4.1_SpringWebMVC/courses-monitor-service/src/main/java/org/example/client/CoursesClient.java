package org.example.client;

import org.example.dto.course.CourseResponse;

import java.util.List;

public interface CoursesClient {

    CourseResponse getCourse(Long id);

    List<CourseResponse> getCourses();

}
