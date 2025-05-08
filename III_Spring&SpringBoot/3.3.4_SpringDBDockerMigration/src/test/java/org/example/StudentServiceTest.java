package org.example;

import org.example.client.CoursesClient;
import org.example.dto.courses.CourseResponse;
import org.example.dto.student.StudentResponse;
import org.example.entity.CoursesEntity;
import org.example.entity.ReviewEntity;
import org.example.entity.StudentEntity;
import org.example.repository.CoursesRepository;
import org.example.repository.ReviewRepository;
import org.example.repository.StudentRepository;
import org.example.service.StudentService;
import org.example.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = StudentServiceImpl.class)
public class StudentServiceTest {

    @Autowired
    private StudentService studentService;
    @MockitoBean
    private StudentRepository studentRepository;
    @MockitoBean
    private CoursesClient coursesClient;
    @MockitoBean
    private CoursesRepository coursesRepository;
    @MockitoBean
    private ReviewRepository reviewRepository;

    private StudentEntity correctStudent =
            new StudentEntity(1L, "Ivan", "Ivanov", null,
                            LocalDate.of(1980, 2, 21),
                        "i_ivanov@mail.ru", new ArrayList<>(), new ArrayList<>());
    private final List<CourseResponse> coursesResponseList =
            List.of(new CourseResponse(1L, "JavaEE", true),
                    new CourseResponse(2L, "Python", true),
                    new CourseResponse(3L, "Csharp", true),
                    new CourseResponse(4L, "C", true),
                    new CourseResponse(5L, "C++", false));

    private final StudentEntity wrongStudent =
            new StudentEntity(10L, null, null, null,
                    null, null, null, null);

    private final CoursesEntity course =
            new CoursesEntity(1L, 2L, new ArrayList<>(), new ArrayList<>());
    private final String courseName = "Python";
    private final StudentEntity correctStudentWithCourse =
            new StudentEntity(correctStudent.getId(), correctStudent.getFirstName(),
                    correctStudent.getLastName(), correctStudent.getPatronymic(),
                    correctStudent.getBirthDate(), correctStudent.getEmail(),
                    new ArrayList<>(), new ArrayList<>(List.of(course)));

    @Test
    public void getExistingStudent() {
        Mockito.when(studentRepository.findById(correctStudent.getId()))
                .thenReturn(Optional.of(correctStudent));
        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);

        Optional<StudentResponse> studentResponseOptional = studentService.getStudent(correctStudent.getId());
        assertTrue(studentResponseOptional.isPresent());
        StudentResponse student = studentResponseOptional.get();
        assertEquals(correctStudent.getId(), student.getId());
        assertEquals(correctStudent.getFirstName(), student.getFirstName());
        assertEquals(correctStudent.getLastName(), student.getLastName());
        assertNull(student.getPatronymic());
        assertEquals(correctStudent.getEmail(), student.getEmail());
    }

    @Test
    public void getNotExistingStudent() {
        Mockito.when(studentRepository.findById(wrongStudent.getId()))
                .thenReturn(Optional.empty());
        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);
        Optional<StudentResponse> studentResponseOptional = studentService.getStudent(wrongStudent.getId());
        assertTrue(studentResponseOptional.isEmpty());
    }

    @Test
    public void createCorrectStudent() {
        correctStudent.setId(null);
        Mockito.when(studentRepository.saveAndFlush(correctStudent))
                .thenReturn(correctStudent);
        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);

        Optional<StudentResponse> studentResponseOptional = studentService.createStudent(correctStudent);
        assertTrue(studentResponseOptional.isPresent());
        StudentResponse student = studentResponseOptional.get();
        assertEquals(correctStudent.getId(), student.getId());
        assertEquals(correctStudent.getFirstName(), student.getFirstName());
        assertEquals(correctStudent.getLastName(), student.getLastName());
        assertNull(student.getPatronymic());
        assertEquals(correctStudent.getEmail(), student.getEmail());
    }

    @Test
    public void createWrongStudent() {
        Optional<StudentResponse> studentResponseOptional = studentService.createStudent(wrongStudent);
        assertTrue(studentResponseOptional.isEmpty());
    }

    @Test
    public void updateCorrectStudent() {
        Mockito.when(studentRepository.existsById(correctStudent.getId()))
                .thenReturn(true);
        Mockito.when(studentRepository.saveAndFlush(correctStudent))
                .thenReturn(correctStudent);
        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);

        Optional<StudentResponse> studentResponseOptional = studentService.updateStudent(correctStudent);
        assertTrue(studentResponseOptional.isPresent());
        StudentResponse student = studentResponseOptional.get();
        assertEquals(correctStudent.getId(), student.getId());
        assertEquals(correctStudent.getFirstName(), student.getFirstName());
        assertEquals(correctStudent.getLastName(), student.getLastName());
        assertNull(student.getPatronymic());
        assertEquals(correctStudent.getEmail(), student.getEmail());
    }

    @Test
    public void updateWrongStudent() {
        Optional<StudentResponse> studentResponseOptional = studentService.updateStudent(wrongStudent);
        assertTrue(studentResponseOptional.isEmpty());
    }

    @Test
    public void deleteExistingStudent() {
        Mockito.when(studentRepository.findById(correctStudent.getId()))
                .thenReturn(Optional.of(correctStudent));
        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);

        Optional<StudentResponse> studentResponseOptional = studentService.deleteStudent(correctStudent.getId());
        assertTrue(studentResponseOptional.isPresent());
        StudentResponse student = studentResponseOptional.get();
        assertEquals(correctStudent.getId(), student.getId());
        assertEquals(correctStudent.getFirstName(), student.getFirstName());
        assertEquals(correctStudent.getLastName(), student.getLastName());
        assertNull(student.getPatronymic());
        assertEquals(correctStudent.getEmail(), student.getEmail());
    }

    @Test
    public void deleteNotExistingStudent() {
        Optional<StudentResponse> studentResponseOptional = studentService.deleteStudent(wrongStudent.getId());
        assertTrue(studentResponseOptional.isEmpty());
    }

    @Test
    public void getStudents() {
        Mockito.when(studentRepository.findAll())
                .thenReturn(List.of(correctStudent));
        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);

        List<StudentResponse> students = studentService.getStudents();
        assertNotNull(students);
        assertEquals(1, students.size());
        StudentResponse student = students.getFirst();
        assertEquals(correctStudent.getId(), student.getId());
        assertEquals(correctStudent.getFirstName(), student.getFirstName());
        assertEquals(correctStudent.getLastName(), student.getLastName());
        assertNull(student.getPatronymic());
        assertEquals(correctStudent.getEmail(), student.getEmail());
    }

    @Test
    public void signUpCorrectCourse() {
        Mockito.when(studentRepository.findById(correctStudent.getId()))
                .thenReturn(Optional.of(correctStudent));
        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);

        CoursesEntity course = new CoursesEntity(1L, 2L, null, new ArrayList<>());
        course.getStudents().add(correctStudentWithCourse);

        Mockito.when(coursesRepository.findByCourseId(course.getCourseId()))
                .thenReturn(course);
        Mockito.when(studentRepository.saveAndFlush(correctStudent))
                .thenReturn(correctStudentWithCourse);

        Optional<StudentResponse> actualStudentOptional =
                studentService.signUpCourse(correctStudent.getId(), coursesResponseList.get(1).getName());
        assertTrue(actualStudentOptional.isPresent());
        StudentResponse student = actualStudentOptional.get();
        assertEquals(correctStudent.getId(), student.getId());
        assertEquals(correctStudent.getFirstName(), student.getFirstName());
        assertEquals(correctStudent.getLastName(), student.getLastName());
        assertNull(student.getPatronymic());
        assertEquals(correctStudent.getEmail(), student.getEmail());
        assertEquals(coursesResponseList.get(1).getName(), student.getCoursesNames().getLast());
    }

    @Test
    public void signUpWrongCourse() {
        Mockito.when(studentRepository.findById(correctStudent.getId()))
                .thenReturn(Optional.of(correctStudent));
        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);

        Optional<StudentResponse> actualStudentOptional =
                studentService.signUpCourse(correctStudent.getId(), "Scala");
        assertTrue(actualStudentOptional.isEmpty());
    }

    @Test
    public void leaveReviewOnCorrectCourse() {
        Mockito.when(studentRepository.findById(correctStudentWithCourse.getId()))
                .thenReturn(Optional.of(correctStudentWithCourse));
        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);

        ReviewEntity review = new ReviewEntity(1L, "great", correctStudentWithCourse, course);
        StudentEntity correctStudentWithReview =
                new StudentEntity(correctStudent.getId(), correctStudent.getFirstName(),
                        correctStudent.getLastName(), correctStudent.getPatronymic(),
                        correctStudent.getBirthDate(), correctStudent.getEmail(),
                        new ArrayList<>(List.of(review)), new ArrayList<>(List.of(course)));

        Mockito.when(studentRepository.saveAndFlush(correctStudentWithCourse))
                .thenReturn(correctStudentWithReview);

        Optional<StudentResponse> actualStudentOptional =
                studentService.leaveReviewOnCourse(correctStudentWithCourse.getId(),
                                                    coursesResponseList.get(1).getName(), review.getText());
        assertTrue(actualStudentOptional.isPresent());
        StudentResponse student = actualStudentOptional.get();
        assertEquals(correctStudent.getId(), student.getId());
        assertEquals(correctStudent.getFirstName(), student.getFirstName());
        assertEquals(correctStudent.getLastName(), student.getLastName());
        assertNull(student.getPatronymic());
        assertEquals(correctStudent.getEmail(), student.getEmail());
        assertEquals(coursesResponseList.get(1).getName(), student.getCoursesNames().getLast());
        assertEquals("Python", student.getReviews().getFirst().getCourseName());
        assertEquals(review.getText(), student.getReviews().getFirst().getText());
    }

    @Test
    public void leaveReviewOnWrongCourse() {
        Mockito.when(studentRepository.findById(correctStudent.getId()))
                .thenReturn(Optional.of(correctStudent));
        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);

        Optional<StudentResponse> actualStudentOptional =
                studentService.leaveReviewOnCourse(correctStudent.getId(),
                        coursesResponseList.get(1).getName(), "no course");
        assertTrue(actualStudentOptional.isEmpty());
    }

    @Test
    public void getStudentsByCorrectCourse() {
        course.getStudents().add(correctStudentWithCourse);

        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);
        Mockito.when(studentRepository.findAll())
                .thenReturn(List.of(correctStudentWithCourse));

        Optional<List<StudentResponse>> studentsOptional =
                studentService.getStudentsByCourse(courseName);
        assertNotNull(studentsOptional);
        List<StudentResponse> students = studentsOptional.get();
        assertEquals(1, students.size());
        StudentResponse actualStudent1 = students.getFirst();
        assertEquals(correctStudentWithCourse.getId(), actualStudent1.getId());
        assertEquals(correctStudentWithCourse.getFirstName(), actualStudent1.getFirstName());
        assertEquals(correctStudentWithCourse.getLastName(), actualStudent1.getLastName());
        assertNull(actualStudent1.getPatronymic());
        assertEquals(correctStudentWithCourse.getEmail(), actualStudent1.getEmail());
        assertEquals(courseName, actualStudent1.getCoursesNames().getFirst());
    }

    @Test
    public void getStudentsByWronCourse() {
        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);

        Optional<List<StudentResponse>> students = studentService.getStudentsByCourse("Scala");
        assertTrue(students.isEmpty());
    }

    @Test
    public void getStudentsOlderThan30() {
        LocalDate date = LocalDate.now().minusYears(30);
        Mockito.when(studentRepository.findStudentsOlderThan(date))
                .thenReturn(List.of(correctStudent));
        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);

        Optional<List<StudentResponse>> studentResponsesOptional =
                studentService.getStudentsOlderThan(30);
        assertTrue(studentResponsesOptional.isPresent());
        StudentResponse student = studentResponsesOptional.get().getFirst();
        assertEquals(correctStudent.getId(), student.getId());
        assertEquals(correctStudent.getFirstName(), student.getFirstName());
        assertEquals(correctStudent.getLastName(), student.getLastName());
        assertNull(student.getPatronymic());
        assertEquals(correctStudent.getEmail(), student.getEmail());
    }

    @Test
    public void getStudentsOlderThanWrong() {
        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);

        Optional<List<StudentResponse>> studentResponsesOptional =
                studentService.getStudentsOlderThan(150);
        assertTrue(studentResponsesOptional.isEmpty());
    }

    @Test
    public void getStudentsByNameAndCourseName() {
        String studentName = correctStudentWithCourse.getFirstName();
        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);
        Mockito.when(studentRepository.findAllByNameAndCourseId(studentName, 2L))
                .thenReturn(List.of(correctStudentWithCourse));

        Optional<List<StudentResponse>> studentResponsesOptional =
                studentService.getStudentsByNameAndCourseName(studentName, courseName);
        assertTrue(studentResponsesOptional.isPresent());
        StudentResponse student = studentResponsesOptional.get().getFirst();
        assertEquals(correctStudentWithCourse.getId(), student.getId());
        assertEquals(correctStudentWithCourse.getFirstName(), student.getFirstName());
        assertEquals(correctStudentWithCourse.getLastName(), student.getLastName());
        assertNull(student.getPatronymic());
        assertEquals(correctStudentWithCourse.getEmail(), student.getEmail());
        assertEquals(courseName, student.getCoursesNames().getFirst());
    }

    @Test
    public void getStudentsByCourseCount() {
        Mockito.when(studentRepository.findStudentsByCourseCount(1L))
                .thenReturn(List.of(correctStudentWithCourse));
        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);

        Optional<List<StudentResponse>> studentResponsesOptional =
                studentService.getStudentsByCourseCount(1L);
        assertTrue(studentResponsesOptional.isPresent());
        StudentResponse student = studentResponsesOptional.get().getFirst();
        assertEquals(correctStudentWithCourse.getId(), student.getId());
        assertEquals(correctStudentWithCourse.getFirstName(), student.getFirstName());
        assertEquals(correctStudentWithCourse.getLastName(), student.getLastName());
        assertNull(student.getPatronymic());
        assertEquals(correctStudentWithCourse.getEmail(), student.getEmail());
        assertEquals(courseName, student.getCoursesNames().getFirst());
    }
}
