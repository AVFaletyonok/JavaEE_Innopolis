package org.example.service.impl;

import lombok.Getter;
import org.example.model.Course;
import org.example.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    @Getter
    private static final List<Course> courses;

    static {
        courses = new ArrayList<>();
        courses.add(new Course(1L, "JavaEE"));
        courses.add(new Course(2L, "Python"));
        courses.add(new Course(3L, "Csharp"));
        courses.add(new Course(4L, "C"));
        courses.add(new Course(5L, "C++"));
    }

    @Override
    public Optional<Course> getCourseByName(final String courseName) {
        for (Course course : courses) {
            if (course.getName().equals(courseName)) {
                return Optional.of(course);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean containsCourse(final String courseName) {
        for (Course course : courses) {
            if (course.getName().equals(courseName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAllCourses(final List<Long> coursesIds) {
        Long courseCount = Long.valueOf(courses.size());
        for (Long courseId : coursesIds) {
            if (courseId <= 0 || courseId > courseCount) {
                return false;
            }
        }
        return true;
    }
}
