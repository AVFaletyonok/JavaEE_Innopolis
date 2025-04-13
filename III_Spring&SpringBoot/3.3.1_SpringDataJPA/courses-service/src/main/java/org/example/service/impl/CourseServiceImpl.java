package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.CoursesResponse;
import org.example.entity.Course;
import org.example.repository.CourseRepository;
import org.example.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    private boolean isCourseCorrect(final Course course) {
        return course != null &&
                course.getName() != null &&
                course.getStartDate().after(new Date());
    }

    private Optional<Course> findCourseByName(final String courseName) {
        List<Course> courses = courseRepository.findAll();
        Optional<Course> courseOptional = courses.stream()
                                            .filter(e -> e.getName().equals(courseName))
                                            .findFirst();
        return courseOptional;
    }

    @Override
    public Optional<CoursesResponse> getCourse(final Long courseId) {
        Optional<Course> course = courseRepository.findById(courseId);
        return course.isPresent()
                ? Optional.of(new CoursesResponse(course.get().getId(),
                                                  course.get().getName(),
                                                  course.get().isActive()))
                : Optional.empty();
    }

    @Override
    public Optional<CoursesResponse> createCourse(Course course) {
        if (!isCourseCorrect(course)) {
            return Optional.empty();
        }
        Optional<Course> courseOptional = findCourseByName(course.getName());
        if (courseOptional.isPresent()) {
            if (!courseOptional.get().isArchived()) {
                return Optional.empty();
            }
            course.setId(courseOptional.get().getId());
            Optional<CoursesResponse> coursesResponseOptional = updateCourse(course);
            return coursesResponseOptional;
        }
        course.setId(null);
        course.setActive(true);
        course.setArchived(false);
        course = courseRepository.saveAndFlush(course);
        return Optional.of(new CoursesResponse(course.getId(), course.getName(), course.isActive()));
    }

    @Override
    public Optional<CoursesResponse> updateCourse(Course course) {
        if (!isCourseCorrect(course) ||
                course.getId() == null || course.getId() <= 0) {
            return Optional.empty();
        }
        Optional<Course> courseOptional = courseRepository.findById(course.getId());
        if (courseOptional.isEmpty()) {
            return Optional.empty();
        }
        Course dbCourse = courseOptional.get();
        dbCourse.setName(course.getName());
        dbCourse.setStartDate(course.getStartDate());
        dbCourse.setActive(course.isActive());
        dbCourse.setArchived(course.isArchived());
        course = courseRepository.saveAndFlush(course);
        return Optional.of(new CoursesResponse(course.getId(), course.getName(), course.isActive()));
    }

    @Override
    public Optional<CoursesResponse> deleteCourse(final Long courseId) {
        if (courseId == null || courseId <= 0) {
            return Optional.empty();
        }
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isEmpty()) {
            return Optional.empty();
        }
        Course course = courseOptional.get();
        course.setActive(false);
        course.setArchived(true);
        course = courseRepository.saveAndFlush(course);
        return Optional.of(new CoursesResponse(course.getId(), course.getName(), course.isActive()));
    }

    @Override
    public Optional<List<CoursesResponse>> getCourses() {
        List<Course> courses = courseRepository.findAll();
        List<CoursesResponse> coursesResponses = courses.stream()
                .map(course -> new CoursesResponse(course.getId(), course.getName(), course.isActive()))
                .collect(Collectors.toList());
        return Optional.of(coursesResponses);
    }
}