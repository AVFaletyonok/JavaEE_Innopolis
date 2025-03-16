package org.example;

import org.example.model.Course;
import org.example.service.CourseService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CourseServiceTest {

    CourseService courseService = new CourseService();

    @Test
    public void getCourses() {
        List<Course> courses = CourseService.getCourses();

        assertEquals(5, courses.size());
        assertEquals("JavaEE", courses.get(0).getName());
        assertEquals("Python", courses.get(1).getName());
        assertEquals("Csharp", courses.get(2).getName());
        assertEquals("C", courses.get(3).getName());
        assertEquals("C++", courses.get(4).getName());
    }

    @Test
    public void getCourseByName() {
        Course javaCourse = courseService.getCourseByName("JavaEE").get();
        assertEquals("JavaEE", javaCourse.getName());
    }

    @Test
    public void getCourseByWrongName() {
        Optional<Course> courseOptional = courseService.getCourseByName("Go");
        assertTrue(courseOptional.isEmpty());
    }

    @Test
    public void containsCourse() {
        assertTrue(courseService.containsCourse("Python"));
    }

    @Test
    public void notContainsCourse() {
        assertFalse(courseService.containsCourse("Scala"));
    }

    @Test
    public void containsAllCourses() {
        assertTrue(courseService.containsAllCourses(List.of(2L, 3L, 5L)));
    }
}
