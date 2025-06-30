package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.client.CoursesClient;
import org.example.dto.courses.CourseResponse;
import org.example.dto.review.ReviewResponse;
import org.example.dto.student.StudentResponse;
import org.example.entity.CoursesEntity;
import org.example.entity.ReviewEntity;
import org.example.entity.StudentEntity;
import org.example.kafka.KafkaProducer;
import org.example.repository.CoursesRepository;
import org.example.repository.ReviewRepository;
import org.example.repository.StudentRepository;
import org.example.service.StudentService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service(value = "studentService")
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    //converters entity to DTO

    private final KafkaProducer kafkaProducer;

    private final StudentRepository studentRepository;
    private final CoursesClient coursesClient;
    private final CoursesRepository coursesRepository;
    private final ReviewRepository reviewRepository;

    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder encoder;

    private static final String EMAIL_REGEX =
            "^[-.0-9a-zA-Z_]{3,30}@[-.0-9a-zA-Z]+\\.[a-zA-Z]{2,4}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private Long convertCourseNameToId(final String courseName) {
        List<CourseResponse> courses = coursesClient.getCourses();
        for (CourseResponse course : courses) {
            if (course.getName().equals(courseName)) {
                return course.getId();
            }
        }
        return null;
    }

    private List<String> convertCoursesIdsToNames(final List<CoursesEntity> coursesEntities) {
        List<String> coursesNames = new ArrayList<>();
        List<CourseResponse> courses = coursesClient.getCourses();
        Map<Long, String> coursesMap = new HashMap<>();
        for (CourseResponse course : courses) {
            coursesMap.put(course.getId(), course.getName());
        }
        int coursesCount = coursesEntities.size();
        for (int i = 0; i < coursesCount; i++) {
            String courseName = coursesMap.get(coursesEntities.get(i).getCourseId());
            coursesNames.add(courseName);
        }
        return coursesNames;
    }

    private List<ReviewResponse> convertReviewToReviewResponse(final List<ReviewEntity> reviews) {
        List<CourseResponse> courses = coursesClient.getCourses();
        Map<Long, String> coursesMap = new HashMap<>();
        for (CourseResponse course : courses) {
            coursesMap.put(course.getId(), course.getName());
        }

        List<ReviewResponse> reviewResponses = reviews.stream()
                                                .map(review -> {
                                                    return new ReviewResponse(
                                                            coursesMap.get(review.getCourse().getCourseId()),
                                                            review.getText());
                                                }).toList();
        return reviewResponses;
    }

    private boolean checkAllCourses(final List<CoursesEntity> coursesEntities) {
        List<CourseResponse> courses = coursesClient.getCourses();
        Set<Long> checkedIds = new HashSet<>();
        Map<Long, String> coursesMap = courses.stream()
                .collect(Collectors.toMap(CourseResponse::getId, CourseResponse::getName));

        for (CoursesEntity course : coursesEntities) {
            if (checkedIds.contains(course.getCourseId()) || !coursesMap.containsKey(course.getCourseId())) {
                return false;
            }
            checkedIds.add(course.getCourseId());
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
                (student.getCourses() == null || checkAllCourses(student.getCourses()));
    }

    private StudentResponse convertToStudentResponse(final StudentEntity student) {
        List<String> coursesNames = convertCoursesIdsToNames(student.getCourses());
        List<ReviewResponse> reviewResponses =
                convertReviewToReviewResponse(student.getReviews());

        return new StudentResponse(student.getId(), student.getFirstName(), student.getLastName(),
                student.getPatronymic(), student.getBirthDate(), student.getEmail(),
                coursesNames, reviewResponses);
    }

    private void registerStudent(final StudentEntity student) {
        userDetailsManager.createUser(new User(student.getEmail(),  encoder.encode(student.getPassword()),
                                        List.of(new SimpleGrantedAuthority("USER"))));
    }

    @Override
    public void sendNotifications(String message, String topic) {
        kafkaProducer.sendMessage(message, topic);
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
        if (student.getReviews() == null) {
            student.setReviews(new ArrayList<>());
        }
        StudentEntity studentEntity = studentRepository.saveAndFlush(student);
        registerStudent(studentEntity);
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
        StudentEntity studentEntity = studentOptional.get();
        StudentResponse studentResponse = convertToStudentResponse(studentEntity);
        studentRepository.deleteById(studentId);
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
        if (student.getCourses().stream()
                .anyMatch(e -> e.getCourseId().equals(courseId))) {
            return Optional.empty();
        }

        CoursesEntity course = coursesRepository.findByCourseId(courseId);
        if (course == null) {
            course = CoursesEntity.builder()
                    .courseId(courseId)
                    .reviews(new ArrayList<>())
                    .students(new ArrayList<>())
                    .build();
        }
        course.getStudents().add(student);
        coursesRepository.saveAndFlush(course);

        StudentEntity studentEntity = studentRepository.saveAndFlush(student);
        StudentResponse studentResponse = convertToStudentResponse(studentEntity);
        return Optional.of(studentResponse);
    }


    @Override
    public Optional<StudentResponse> leaveReviewOnCourse(final Long studentId, final String courseName, final String review) {
        if (studentId == null || studentId <= 0 || courseName == null ||
                review == null || review.isBlank()) {
            return Optional.empty();
        };
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
            return Optional.empty();
        }
        Optional<CoursesEntity> courseOpt = student.getCourses().stream()
                .filter(e -> e.getCourseId().equals(courseId))
                .findFirst();
        if (courseOpt.isEmpty()) {
            return Optional.empty();
        }
        CoursesEntity course = courseOpt.get();
        ReviewEntity newReviewObj = ReviewEntity.builder()
                .text(review)
                .student(student)
                .course(course)
                .build();
        course.getReviews().add(newReviewObj);

        reviewRepository.saveAndFlush(newReviewObj);
        coursesRepository.saveAndFlush(course);
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
                    return student.getCourses().stream()
                            .map(CoursesEntity::getCourseId)
                            .toList()
                            .contains(courseId);
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
