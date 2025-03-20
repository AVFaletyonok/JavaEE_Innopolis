package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.model.Student;
import org.example.repository.StudentRepository;
import org.example.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service(value = "studentService")
@RequiredArgsConstructor
//@ConditionalOnProperty(prefix = "mock", name = "student.service", havingValue = "false")
//@Profile("!mock")
public class StudentServiceImpl implements StudentService {

    //converters entity to DTO

    private final StudentRepository studentRepository;
    @Autowired
    private final CourseServiceImpl courseService;

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

    @Override
    public boolean isStudentCorrect(final Student student) {
        return student.getFirstName() != null && student.getLastName() != null &&
                isEmailCorrect(student.getEmail()) &&
                (student.getCourses() == null ||
                courseService.containsAllCourses(student.getCourses()));
    }

    @Override
    public Optional<Student> getStudent(final Long studentId) {
        if (studentId <= 0) {
            return Optional.empty();
        }
        return studentRepository.findById(studentId);
    }

    @Override
    public Optional<Student> createStudent(final Student student) {
        if (!isStudentCorrect(student)) {
            return Optional.empty();
        }
        Student resultStudent = studentRepository.saveAndFlush(student);
        return Optional.of(resultStudent);
    }

    @Override
    public Optional<Student> updateStudent(final Student student) {
        if (student.getId() == null || student.getId() <= 0 ||
                !isStudentCorrect(student)) {
            return Optional.empty();
        }
        if (!studentRepository.existsById(student.getId())) {
            return Optional.empty();
        }
        Student newStudent = studentRepository.saveAndFlush(student);
        return Optional.of(newStudent);
    }

    @Override
    public Optional<Student> deleteStudent(final Long studentId) {
        if (studentId <= 0) {
            return Optional.empty();
        }
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            return Optional.empty();
        }
        studentRepository.deleteById(studentId);
        return student;
    }

    @Override
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> signUpCourse(final Long studentId, final String courseName) {
        if (studentId == null || studentId <= 0 || courseName == null) {
            return Optional.empty();
        }
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
