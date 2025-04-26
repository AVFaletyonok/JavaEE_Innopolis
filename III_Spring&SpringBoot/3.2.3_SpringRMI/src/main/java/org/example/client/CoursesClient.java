package org.example.client;

import org.example.dto.courses.CoursesResponse;

import java.util.List;

public interface CoursesClient {

    CoursesResponse getCourse(Long id);

    List<CoursesResponse> getCourses();

}
