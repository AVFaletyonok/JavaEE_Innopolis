package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.client.CoursesClient;
import org.example.dto.courses.CoursesResponse;
import org.example.dto.student.StudentResponse;
import org.example.entity.StudentEntity;
import org.example.entity.StudentGroupEntity;
import org.example.repository.StudentGroupRepository;
import org.example.repository.StudentRepository;
import org.example.service.StudentService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    private final StudentGroupRepository studentGroupRepository;

    private static final String EMAIL_REGEX =
            "^[-.0-9a-zA-Z_]{3,30}@[-.0-9a-zA-Z]+\\.[a-zA-Z]{2,4}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private Long convertCourseNameToId(final String courseName) {
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

    private boolean isStudentCorrect(final StudentEntity student) {
        LocalDate currectDate = LocalDate.now();
        LocalDate maxAgeDate = LocalDate.of(1900, 1, 1);
        return student.getFirstName() != null && student.getLastName() != null &&
                student.getBirthDate() != null &&
                student.getBirthDate().isBefore(currectDate) &&
                student.getBirthDate().isAfter(maxAgeDate) &&
                isEmailCorrect(student.getEmail()) &&
                student.getStudentGroup() != null && student.getStudentGroup().getName() != null &&
                studentGroupRepository.isGroupExists(student.getStudentGroup().getName()) &&
                (student.getCourses() == null || checkAllCourses(student.getCourses()));
    }

    private StudentResponse convertToStudentResponse(final StudentEntity student) {
        List<String> coursesNames = convertCoursesIdsToNames(student.getCourses());
        return new StudentResponse(student.getId(), student.getFirstName(), student.getLastName(),
                student.getPatronymic(), student.getBirthDate(), student.getEmail(),
                student.getStudentGroup().getName(), coursesNames);
    }

    @Override
    public Optional<StudentResponse> getStudent(final Long studentId) {
        if (studentId == null || studentId <= 0) {
            return Optional.empty();
        }
        Optional<StudentEntity> studentOptional = studentRepository.findById(studentId);
        if (studentOptional.isEmpty()) {
            return Optional.empty();
        }
        StudentEntity student = studentOptional.get();
        StudentResponse studentResponse = convertToStudentResponse(student);
        return Optional.of(studentResponse);
    }

    @Override
    public Optional<StudentResponse> createStudent(final StudentEntity student) {
        if (!isStudentCorrect(student)) {
            return Optional.empty();
        }
        student.setId(null);
        if (student.getCourses() == null) {
            student.setCourses(new ArrayList<>());
        }
        StudentGroupEntity studentGroup = studentGroupRepository.findByName(student.getStudentGroup().getName());
        studentGroup.addStudent(student);
        studentGroupRepository.saveAndFlush(studentGroup);
        StudentEntity studentEntity = studentRepository.saveAndFlush(student);
        StudentResponse studentResponse = convertToStudentResponse(studentEntity);
        return Optional.of(studentResponse);
    }

    @Override
    public Optional<StudentResponse> updateStudent(final StudentEntity student) {
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
        StudentGroupEntity studentGroup = studentGroupRepository.findByName(student.getStudentGroup().getName());
        studentGroup.addStudent(student);
        studentGroupRepository.saveAndFlush(studentGroup);
        StudentEntity studentEntity = studentRepository.saveAndFlush(student);
        StudentResponse studentResponse = convertToStudentResponse(studentEntity);
        return Optional.of(studentResponse);
    }

    @Override
    public Optional<StudentResponse> deleteStudent(final Long studentId) {
        if (studentId == null || studentId <= 0) {
            return Optional.empty();
        }
        Optional<StudentEntity> studentOptional = studentRepository.findById(studentId);
        if (studentOptional.isEmpty()) {
            return Optional.empty();
        }
        studentRepository.deleteById(studentId);
        StudentEntity studentEntity = studentOptional.get();
        StudentResponse studentResponse = convertToStudentResponse(studentEntity);
        return Optional.of(studentResponse);
    }

    @Override
    public List<StudentResponse> getStudents() {
//        Specification<StudentEntity> studentEntitySpecification = getSpecification("Олег");
//        Pageable pageable = PageRequest.of(0, 100, Sort.by(Sort.Order.asc("id")));
//        List<StudentEntity> students = studentRepository.findAll(pageable);
        List<StudentEntity> students = studentRepository.findAll();
        List<StudentResponse> studentResponses = students.stream()
                .map(student -> { return convertToStudentResponse(student); }).toList();
        return studentResponses;
    }

//    private Specification<StudentEntity> getSpecification(String name) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), name);
//    }

    @Override
    public Optional<StudentResponse> signUpCourse(final Long studentId, final String courseName) {
        if (studentId == null || studentId <= 0 || courseName == null) {
            return Optional.empty();
        }
        Optional<StudentEntity> studentOptional = studentRepository.findById(studentId);
        if (studentOptional.isEmpty()) {
            return Optional.empty();
        }
        Long courseId = convertCourseNameToId(courseName);
        if (courseId == null) {
            return Optional.empty();
        }
        StudentEntity student = studentOptional.get();
        if (student.getCourses() == null) {
            student.setCourses(new ArrayList<>());
        }
        if (student.getCourses().contains(courseId)) {
            return Optional.empty();
        }
        student.getCourses().add(courseId);
        StudentEntity studentEntity = studentRepository.saveAndFlush(student);
        StudentResponse studentResponse = convertToStudentResponse(studentEntity);
        return Optional.of(studentResponse);
    }

    @Override
    public Optional<List<StudentResponse>> getStudentsByCourse(final String courseName) {
        Long courseId = convertCourseNameToId(courseName);
        if (courseId == null) {
            return Optional.empty();
        }
        List<StudentEntity> students = studentRepository.findAll();
        List<StudentResponse> studentResponses = students.stream()
                .filter(student -> {
                    return student.getCourses().contains(courseId);
                })
                .map(student -> { return convertToStudentResponse(student); }).toList();
        return Optional.of(studentResponses);
    }

    @Override
    public Optional<List<StudentResponse>> getStudentsOlderThan(final Integer age) {
        if (age == null || age <= 0 || age > 125) {
            return Optional.empty();
        }
        LocalDate dateNow = LocalDate.now();
        LocalDate date = dateNow.minusYears(age);
        List<StudentEntity> students = studentRepository.findStudentsOlderThan(date);
        List<StudentResponse> studentResponses = students.stream()
                .map(student -> { return convertToStudentResponse(student); }).toList();
        return Optional.of(studentResponses);
    }

    @Override
    public Optional<List<StudentResponse>> getStudentsByNameAndCourseName(final String studentName, final String courseName) {
        if (studentName == null || courseName == null) {
            return Optional.empty();
        }
        Long courseId = convertCourseNameToId(courseName);
        List<StudentEntity> students = studentRepository.findAllByNameAndCourseId(studentName, courseId);
        List<StudentResponse> studentResponses = students.stream()
                .map(student -> { return convertToStudentResponse(student); }).toList();
        return Optional.of(studentResponses);
    }

    @Override
    public Optional<List<StudentResponse>> getStudentsByCourseCount(final Long courseCount) {
        if (courseCount == null || courseCount <= 0) {
            return Optional.empty();
        }
        List<StudentEntity> students = studentRepository.findStudentsByCourseCount(courseCount);
        List<StudentResponse> studentResponses = students.stream()
                .map(student -> { return convertToStudentResponse(student); }).toList();
        return Optional.of(studentResponses);
    }
}
