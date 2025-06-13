package org.example.initializer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.entity.Course;
import org.example.repository.CourseRepository;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final CourseRepository courseRepository;

    @PostConstruct
    public void init() {
        if (courseRepository.count() == 0) {
            courseRepository.saveAndFlush(new Course(null, "JavaEE", new Date(), true, false));
            courseRepository.saveAndFlush(new Course(null, "Python", new Date(), true, false));
            courseRepository.saveAndFlush(new Course(null, "Csharp", new Date(), true, false));
            courseRepository.saveAndFlush(new Course(null, "C", new Date(), true, false));
            courseRepository.saveAndFlush(new Course(null, "C++", new Date(), false, true));
        }
    }
}
