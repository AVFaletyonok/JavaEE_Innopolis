package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.Student;
import org.example.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    @Autowired
    private final CourseService courseService;

    private static final String EMAIL_REGEX =
            "^[-.0-9a-zA-Z_]{3,30}@[-.0-9a-zA-Z]+\\.[a-zA-Z]{2,4}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public static boolean isEmailCorrect(String email) {
        if (email == null) {
            return false;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    public boolean isStudentCorrect(Student student) {
        return student.getFirstName() != null && student.getLastName() != null &&
                isEmailCorrect(student.getEmail()) &&
                (student.getCourses() == null ||
                courseService.containsAllCourses(student.getCourses()));
    }

    public Optional<Student> signUpCourse(Long studentId, String courseName) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if (studentOptional.isEmpty() ||
                !courseService.containsCourse(courseName)) {
            return Optional.empty();
        }
        Long courseId = courseService.getCourseByName(courseName).get().getId();
        Student student = studentOptional.get();
        if (student.getCourses() == null) {
            student.setCourses(new ArrayList<>());
        }
        if (student.getCourses().contains(courseId)) {
            return Optional.empty();
        }
        student.getCourses().add(courseId);
        studentRepository.saveAndFlush(student);
        return Optional.of(student);
    }
}
