package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.client.CoursesClient;
import org.example.dto.courses.CoursesResponse;
import org.example.dto.student.StudentResponse;
import org.example.entity.Student;
import org.example.repository.StudentRepository;
import org.example.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service(value = "studentService")
@RequiredArgsConstructor
//@ConditionalOnProperty(prefix = "mock", name = "student.service", havingValue = "false")
//@Profile("!mock")
public class StudentServiceImpl implements StudentService {

    //converters entity to DTO

    private final StudentRepository studentRepository;
    private final CoursesClient coursesClient;

    private static final String EMAIL_REGEX =
            "^[-.0-9a-zA-Z_]{3,30}@[-.0-9a-zA-Z]+\\.[a-zA-Z]{2,4}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private Long converCourseNameToId(final String courseName) {
        List<CoursesResponse> courses = coursesClient.getCourses();
        for (CoursesResponse course : courses) {
            if (course.getName().equals(courseName)) {
                return course.getId();
            }
        }
        return null;
    }

    private List<String> convertCoursesIdsToNames(final List<Long> coursesIds) {
        List<String> coursesNames = new ArrayList<>();
        List<CoursesResponse> courses = coursesClient.getCourses();
        Map<Long, String> coursesMap = new HashMap<>();
        for (CoursesResponse course : courses) {
            coursesMap.put(course.getId(), course.getName());
        }
        int coursesCount = coursesIds.size();
        for (int i = 0; i < coursesCount; i++) {
            String courseName = coursesMap.get(coursesIds.get(i));
            coursesNames.add(courseName);
        }
        return coursesNames;
    }

    private boolean checkAllCourses(final List<Long> coursesIds) {
        List<CoursesResponse> courses = coursesClient.getCourses();
        Set<Long> checkedIds = new HashSet<>();
        Map<Long, String> coursesMap = courses.stream()
                .collect(Collectors.toMap(CoursesResponse::getId, CoursesResponse::getName));

        for (Long courseId : coursesIds) {
            if (checkedIds.contains(courseId) || !coursesMap.containsKey(courseId)) {
                return false;
            }
            checkedIds.add(courseId);
        }
        return true;
    }

    private boolean isEmailCorrect(String email) {
        if (email == null) {
            return false;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    private boolean isStudentCorrect(final Student student) {
        return student.getFirstName() != null && student.getLastName() != null &&
                isEmailCorrect(student.getEmail()) &&
                (student.getCourses() == null || checkAllCourses(student.getCourses()));
    }

    @Override
    public Optional<StudentResponse> getStudent(final Long studentId) {
        if (studentId <= 0) {
            return Optional.empty();
        }
        Optional<Student> studentEntity = studentRepository.findById(studentId);
        if (studentEntity.isEmpty()) {
            return Optional.empty();
        }
        Student student = studentEntity.get();
        List<String> coursesNames = convertCoursesIdsToNames(student.getCourses());
        StudentResponse studentResponse =
                new StudentResponse(student.getId(), student.getFirstName(), student.getLastName(),
                                    student.getPatronymic(), student.getEmail(), coursesNames);
        return Optional.of(studentResponse);
    }

    @Override
    public Optional<StudentResponse> createStudent(final Student student) {
        if (!isStudentCorrect(student)) {
            return Optional.empty();
        }
        student.setId(null);
        if (student.getCourses() == null) {
            student.setCourses(new ArrayList<>());
        }
        Student studentEntity = studentRepository.saveAndFlush(student);
        List<String> coursesNames = convertCoursesIdsToNames(studentEntity.getCourses());
        StudentResponse studentResponse =
                new StudentResponse(studentEntity.getId(), studentEntity.getFirstName(), studentEntity.getLastName(),
                                    studentEntity.getPatronymic(), studentEntity.getEmail(), coursesNames);
        return Optional.of(studentResponse);
    }

    @Override
    public Optional<StudentResponse> updateStudent(final Student student) {
        if (student.getId() == null || student.getId() <= 0 ||
                !isStudentCorrect(student)) {
            return Optional.empty();
        }
        if (!studentRepository.existsById(student.getId())) {
            return Optional.empty();
        }
        if (student.getCourses() == null) {
            student.setCourses(new ArrayList<>());
        }
        Student studentEntity = studentRepository.saveAndFlush(student);
        List<String> coursesNames = convertCoursesIdsToNames(studentEntity.getCourses());
        StudentResponse studentResponse =
                new StudentResponse(studentEntity.getId(), studentEntity.getFirstName(), studentEntity.getLastName(),
                        studentEntity.getPatronymic(), studentEntity.getEmail(), coursesNames);
        return Optional.of(studentResponse);
    }

    @Override
    public Optional<StudentResponse> deleteStudent(final Long studentId) {
        if (studentId == null || studentId <= 0) {
            return Optional.empty();
        }
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if (studentOptional.isEmpty()) {
            return Optional.empty();
        }
        studentRepository.deleteById(studentId);
        Student studentEntity = studentOptional.get();
        List<String> coursesNames = convertCoursesIdsToNames(studentEntity.getCourses());
        StudentResponse studentResponse =
                new StudentResponse(studentEntity.getId(), studentEntity.getFirstName(), studentEntity.getLastName(),
                        studentEntity.getPatronymic(), studentEntity.getEmail(), coursesNames);
        return Optional.of(studentResponse);
    }

    @Override
    public List<StudentResponse> getStudents() {
        List<Student> students = studentRepository.findAll();
        List<StudentResponse> studentResponses = students.stream()
                .map(student -> {
                    List<String> coursesNames = convertCoursesIdsToNames(student.getCourses());
                    return new StudentResponse(student.getId(), student.getFirstName(),
                                        student.getLastName(), student.getPatronymic(),
                                        student.getEmail(), coursesNames);
                }).toList();
        return studentResponses;
    }

    @Override
    public Optional<StudentResponse> signUpCourse(final Long studentId, final String courseName) {
        if (studentId == null || studentId <= 0 || courseName == null) {
            return Optional.empty();
        }
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if (studentOptional.isEmpty()) {
            return Optional.empty();
        }
        Long courseId = converCourseNameToId(courseName);
        if (courseId == null) {
            return Optional.empty();
        }
        Student student = studentOptional.get();
        if (student.getCourses() == null) {
            student.setCourses(new ArrayList<>());
        }
        if (student.getCourses().contains(courseId)) {
            return Optional.empty();
        }
        student.getCourses().add(courseId);
        Student studentEntity = studentRepository.saveAndFlush(student);
        List<String> coursesNames = convertCoursesIdsToNames(studentEntity.getCourses());
        StudentResponse studentResponse =
                new StudentResponse(studentEntity.getId(), studentEntity.getFirstName(), studentEntity.getLastName(),
                        studentEntity.getPatronymic(), studentEntity.getEmail(), coursesNames);
        return Optional.of(studentResponse);
    }
}
