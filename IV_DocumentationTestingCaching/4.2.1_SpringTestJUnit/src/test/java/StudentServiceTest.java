import lombok.extern.slf4j.Slf4j;
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
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockConstruction;

@Slf4j
@SpringBootTest(classes = StudentServiceImpl.class)
public class StudentServiceTest {

    @Autowired
    private StudentService studentService;
    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private CoursesClient coursesClient;
    @MockBean
    private CoursesRepository coursesRepository;
    @MockBean
    private ReviewRepository reviewRepository;
    @MockBean
    private UserDetailsManager userDetailsManager;
    @MockBean
    private PasswordEncoder encoder;

    private StudentEntity correctStudent =
            new StudentEntity(1L, "Ivan", "Ivanov", null,
                            LocalDate.of(1980, 2, 21),
                        "i_ivanov@mail.ru", "123456", "123456",
                            new ArrayList<>(), new ArrayList<>());
    private final List<CourseResponse> coursesResponseList =
            List.of(new CourseResponse(1L, "JavaEE", new Date(), true),
                    new CourseResponse(2L, "Python", new Date(), true),
                    new CourseResponse(3L, "Csharp", new Date(), true),
                    new CourseResponse(4L, "C", new Date(), true),
                    new CourseResponse(5L, "C++", new Date(), false));

    private final StudentEntity wrongStudent =
            new StudentEntity(10L, null, null, null,
                    null, null, null, null, null, null);

    private final CoursesEntity course =
            new CoursesEntity(1L, 2L, new ArrayList<>(), new ArrayList<>());
    private final String courseName = "Python";
    private final StudentEntity correctStudentWithCourse =
            new StudentEntity(correctStudent.getId(), correctStudent.getFirstName(),
                    correctStudent.getLastName(), correctStudent.getPatronymic(),
                    correctStudent.getBirthDate(), correctStudent.getEmail(),
                    correctStudent.getPassword(), correctStudent.getPasswordConfirm(),
                    new ArrayList<>(), new ArrayList<>(List.of(course)));

    @Test
    public void getExistingStudent() {
        // Arrange
        log.info("getExistingStudent test has started.");
        Mockito.when(studentRepository.findById(correctStudent.getId()))
                .thenReturn(Optional.of(correctStudent));
        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);

        // Act
        Optional<StudentResponse> studentResponseOptional = studentService.getStudent(correctStudent.getId());

        // Assert
        assertTrue(studentResponseOptional.isPresent());
        StudentResponse student = studentResponseOptional.get();
        log.info("Student : " + student.toString());
        assertEquals(correctStudent.getId(), student.getId());
        assertEquals(correctStudent.getFirstName(), student.getFirstName());
        assertEquals(correctStudent.getLastName(), student.getLastName());
        assertNull(student.getPatronymic());
        assertEquals(correctStudent.getEmail(), student.getEmail());
        log.info("getExistingStudent test has successfully finished.");
    }

    @Test
    public void getNotExistingStudent() {
        // Arrange
        log.info("getNotExistingStudent test has started.");
        Mockito.when(studentRepository.findById(wrongStudent.getId()))
                .thenReturn(Optional.empty());
        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);

        // Act
        Optional<StudentResponse> studentResponseOptional = studentService.getStudent(wrongStudent.getId());

        // Assert
        assertTrue(studentResponseOptional.isEmpty());
        log.info("getNotExistingStudent test has successfully finished.");
    }

    @Test
    public void createCorrectStudent() {
        // Arrange
        log.info("createCorrectStudent test has started.");
        correctStudent.setId(null);
        Mockito.when(studentRepository.saveAndFlush(correctStudent))
                .thenReturn(correctStudent);
        doNothing().when(userDetailsManager).createUser(any(User.class));
        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);

        // Мокируем вызов приватного метода
        try (MockedConstruction<User> mockedUser = mockConstruction(User.class)) {
            doNothing().when(userDetailsManager).createUser(any(User.class));

            // Act
            Optional<StudentResponse> studentResponseOptional = studentService.createStudent(correctStudent);

            // Assert
            assertTrue(studentResponseOptional.isPresent());
            StudentResponse student = studentResponseOptional.get();
            log.info("Student : " + student.toString());
            assertEquals(correctStudent.getId(), student.getId());
            assertEquals(correctStudent.getFirstName(), student.getFirstName());
            assertEquals(correctStudent.getLastName(), student.getLastName());
            assertNull(student.getPatronymic());
            assertEquals(correctStudent.getEmail(), student.getEmail());
            log.info("createCorrectStudent test has successfully finished.");
        }
    }

    @Test
    public void createWrongStudent() {
        // Arrange
        log.info("createWrongStudent test has started.");

        // Act
        Optional<StudentResponse> studentResponseOptional = studentService.createStudent(wrongStudent);

        // Assert
        assertTrue(studentResponseOptional.isEmpty());
        log.info("createWrongStudent test has successfully finished.");
    }

    @Test
    public void updateCorrectStudent() {
        // Arrange
        log.info("updateCorrectStudent test has started.");
        Mockito.when(studentRepository.existsById(correctStudent.getId()))
                .thenReturn(true);
        Mockito.when(studentRepository.saveAndFlush(correctStudent))
                .thenReturn(correctStudent);
        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);

        // Act
        Optional<StudentResponse> studentResponseOptional = studentService.updateStudent(correctStudent);

        // Assert
        assertTrue(studentResponseOptional.isPresent());
        StudentResponse student = studentResponseOptional.get();
        log.info("Student : " + student.toString());
        assertEquals(correctStudent.getId(), student.getId());
        assertEquals(correctStudent.getFirstName(), student.getFirstName());
        assertEquals(correctStudent.getLastName(), student.getLastName());
        assertNull(student.getPatronymic());
        assertEquals(correctStudent.getEmail(), student.getEmail());
        log.info("updateCorrectStudent test has successfully finished.");
    }

    @Test
    public void updateWrongStudent() {
        // Arrange
        log.info("updateWrongStudent test has started.");

        // Act
        Optional<StudentResponse> studentResponseOptional = studentService.updateStudent(wrongStudent);

        // Assert
        assertTrue(studentResponseOptional.isEmpty());
        log.info("updateWrongStudent test has successfully finished.");
    }

    @Test
    public void deleteExistingStudent() {
        // Arrange
        log.info("deleteExistingStudent test has started.");
        Mockito.when(studentRepository.findById(correctStudent.getId()))
                .thenReturn(Optional.of(correctStudent));
        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);

        // Act
        Optional<StudentResponse> studentResponseOptional = studentService.deleteStudent(correctStudent.getId());

        // Assert
        assertTrue(studentResponseOptional.isPresent());
        StudentResponse student = studentResponseOptional.get();
        log.info("Student : " + student.toString());
        assertEquals(correctStudent.getId(), student.getId());
        assertEquals(correctStudent.getFirstName(), student.getFirstName());
        assertEquals(correctStudent.getLastName(), student.getLastName());
        assertNull(student.getPatronymic());
        assertEquals(correctStudent.getEmail(), student.getEmail());
        log.info("deleteExistingStudent test has successfully finished.");
    }

    @Test
    public void deleteNotExistingStudent() {
        // Arrange
        log.info("deleteNotExistingStudent test has started.");

        // Act
        Optional<StudentResponse> studentResponseOptional = studentService.deleteStudent(wrongStudent.getId());

        // Assert
        assertTrue(studentResponseOptional.isEmpty());
        log.info("deleteNotExistingStudent test has successfully finished.");
    }

    @Test
    public void getStudents() {
        // Arrange
        log.info("getStudents test has started.");
        Mockito.when(studentRepository.findAll())
                .thenReturn(List.of(correctStudent));
        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);

        // Act
        List<StudentResponse> students = studentService.getStudents();

        // Assert
        assertNotNull(students);
        assertEquals(1, students.size());
        StudentResponse student = students.get(0);
        log.info("Student : " + student.toString());
        assertEquals(correctStudent.getId(), student.getId());
        assertEquals(correctStudent.getFirstName(), student.getFirstName());
        assertEquals(correctStudent.getLastName(), student.getLastName());
        assertNull(student.getPatronymic());
        assertEquals(correctStudent.getEmail(), student.getEmail());
        log.info("getStudents test has successfully finished.");
    }

    @Test
    public void signUpCorrectCourse() {
        // Arrange
        log.info("signUpCorrectCourse test has started.");
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

        // Act
        Optional<StudentResponse> actualStudentOptional =
                studentService.signUpCourse(correctStudent.getId(), coursesResponseList.get(1).getName());

        // Assert
        assertTrue(actualStudentOptional.isPresent());
        StudentResponse student = actualStudentOptional.get();
        log.info("Student : " + student.toString());
        assertEquals(correctStudent.getId(), student.getId());
        assertEquals(correctStudent.getFirstName(), student.getFirstName());
        assertEquals(correctStudent.getLastName(), student.getLastName());
        assertNull(student.getPatronymic());
        assertEquals(correctStudent.getEmail(), student.getEmail());
        assertEquals(coursesResponseList.get(1).getName(), student.getCoursesNames().get(0));
        log.info("signUpCorrectCourse test has successfully finished.");
    }

    @Test
    public void signUpWrongCourse() {
        // Arrange
        log.info("signUpWrongCourse test has started.");
        Mockito.when(studentRepository.findById(correctStudent.getId()))
                .thenReturn(Optional.of(correctStudent));
        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);

        // Act
        Optional<StudentResponse> actualStudentOptional =
                studentService.signUpCourse(correctStudent.getId(), "Scala");

        // Assert
        assertTrue(actualStudentOptional.isEmpty());
        log.info("signUpWrongCourse test has successfully finished.");
    }

    @Test
    public void leaveReviewOnCorrectCourse() {
        // Arrange
        log.info("leaveReviewOnCorrectCourse test has started.");
        Mockito.when(studentRepository.findById(correctStudentWithCourse.getId()))
                .thenReturn(Optional.of(correctStudentWithCourse));
        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);

        ReviewEntity review = new ReviewEntity(1L, "great", correctStudentWithCourse, course);
        StudentEntity correctStudentWithReview =
                new StudentEntity(correctStudent.getId(), correctStudent.getFirstName(),
                        correctStudent.getLastName(), correctStudent.getPatronymic(),
                        correctStudent.getBirthDate(), correctStudent.getEmail(),
                        correctStudent.getPassword(), correctStudent.getPasswordConfirm(),
                        new ArrayList<>(List.of(review)), new ArrayList<>(List.of(course)));

        Mockito.when(studentRepository.saveAndFlush(correctStudentWithCourse))
                .thenReturn(correctStudentWithReview);

        // Act
        Optional<StudentResponse> actualStudentOptional =
                studentService.leaveReviewOnCourse(correctStudentWithCourse.getId(),
                                                    coursesResponseList.get(1).getName(), review.getText());

        // Assert
        assertTrue(actualStudentOptional.isPresent());
        StudentResponse student = actualStudentOptional.get();
        log.info("Student : " + student.toString());
        assertEquals(correctStudent.getId(), student.getId());
        assertEquals(correctStudent.getFirstName(), student.getFirstName());
        assertEquals(correctStudent.getLastName(), student.getLastName());
        assertNull(student.getPatronymic());
        assertEquals(correctStudent.getEmail(), student.getEmail());
        assertEquals(coursesResponseList.get(1).getName(), student.getCoursesNames().get(0));
        assertEquals("Python", student.getReviews().get(0).getCourseName());
        assertEquals(review.getText(), student.getReviews().get(0).getText());
        log.info("leaveReviewOnCorrectCourse test has successfully finished.");
    }

    @Test
    public void leaveReviewOnWrongCourse() {
        // Arrange
        log.info("leaveReviewOnWrongCourse test has started.");
        Mockito.when(studentRepository.findById(correctStudent.getId()))
                .thenReturn(Optional.of(correctStudent));
        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);

        // Act
        Optional<StudentResponse> actualStudentOptional =
                studentService.leaveReviewOnCourse(correctStudent.getId(),
                        coursesResponseList.get(1).getName(), "no course");

        // Assert
        assertTrue(actualStudentOptional.isEmpty());
        log.info("leaveReviewOnWrongCourse test has successfully finished.");
    }

    @Test
    public void getStudentsByCorrectCourse() {
        // Arrange
        log.info("getStudentsByCorrectCourse test has started.");
        course.getStudents().add(correctStudentWithCourse);

        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);
        Mockito.when(studentRepository.findAll())
                .thenReturn(List.of(correctStudentWithCourse));

        // Act
        Optional<List<StudentResponse>> studentsOptional =
                studentService.getStudentsByCourse(courseName);

        // Assert
        assertNotNull(studentsOptional);
        List<StudentResponse> students = studentsOptional.get();
        assertEquals(1, students.size());
        StudentResponse actualStudent1 = students.get(0);
        log.info("Student : " + actualStudent1);
        assertEquals(correctStudentWithCourse.getId(), actualStudent1.getId());
        assertEquals(correctStudentWithCourse.getFirstName(), actualStudent1.getFirstName());
        assertEquals(correctStudentWithCourse.getLastName(), actualStudent1.getLastName());
        assertNull(actualStudent1.getPatronymic());
        assertEquals(correctStudentWithCourse.getEmail(), actualStudent1.getEmail());
        assertEquals(courseName, actualStudent1.getCoursesNames().get(0));
        log.info("getStudentsByCorrectCourse test has successfully finished.");
    }

    @Test
    public void getStudentsByWronCourse() {
        // Arrange
        log.info("getStudentsByWronCourse test has started.");
        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);

        // Act
        Optional<List<StudentResponse>> students = studentService.getStudentsByCourse("Scala");

        // Assert
        assertTrue(students.isEmpty());
        log.info("getStudentsByWronCourse test has successfully finished.");
    }

    @Test
    public void getStudentsOlderThan30() {
        // Arrange
        log.info("getStudentsOlderThan30 test has started.");
        LocalDate date = LocalDate.now().minusYears(30);
        Mockito.when(studentRepository.findStudentsOlderThan(date))
                .thenReturn(List.of(correctStudent));
        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);

        // Act
        Optional<List<StudentResponse>> studentResponsesOptional =
                studentService.getStudentsOlderThan(30);

        // Assert
        assertTrue(studentResponsesOptional.isPresent());
        StudentResponse student = studentResponsesOptional.get().get(0);
        log.info("Student : " + student.toString());
        assertEquals(correctStudent.getId(), student.getId());
        assertEquals(correctStudent.getFirstName(), student.getFirstName());
        assertEquals(correctStudent.getLastName(), student.getLastName());
        assertNull(student.getPatronymic());
        assertEquals(correctStudent.getEmail(), student.getEmail());
        log.info("getStudentsOlderThan30 test has successfully finished.");
    }

    @Test
    public void getStudentsOlderThanWrong() {
        // Arrange
        log.info("getStudentsOlderThanWrong test has started.");
        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);

        // Act
        Optional<List<StudentResponse>> studentResponsesOptional =
                studentService.getStudentsOlderThan(150);

        // Assert
        assertTrue(studentResponsesOptional.isEmpty());
        log.info("getStudentsOlderThanWrong test has successfully finished.");
    }

    @Test
    public void getStudentsByNameAndCourseName() {
        // Arrange
        log.info("getStudentsByNameAndCourseName test has started.");
        String studentName = correctStudentWithCourse.getFirstName();
        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);
        Mockito.when(studentRepository.findAllByNameAndCourseId(studentName, 2L))
                .thenReturn(List.of(correctStudentWithCourse));

        // Act
        Optional<List<StudentResponse>> studentResponsesOptional =
                studentService.getStudentsByNameAndCourseName(studentName, courseName);

        // Assert
        assertTrue(studentResponsesOptional.isPresent());
        StudentResponse student = studentResponsesOptional.get().get(0);
        log.info("Student : " + student.toString());
        assertEquals(correctStudentWithCourse.getId(), student.getId());
        assertEquals(correctStudentWithCourse.getFirstName(), student.getFirstName());
        assertEquals(correctStudentWithCourse.getLastName(), student.getLastName());
        assertNull(student.getPatronymic());
        assertEquals(correctStudentWithCourse.getEmail(), student.getEmail());
        assertEquals(courseName, student.getCoursesNames().get(0));
        log.info("getStudentsByNameAndCourseName test has successfully finished.");
    }

    @Test
    public void getStudentsByCourseCount() {
        // Arrange
        log.info("getStudentsByCourseCount test has started.");
        Mockito.when(studentRepository.findStudentsByCourseCount(1L))
                .thenReturn(List.of(correctStudentWithCourse));
        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);

        // Act
        Optional<List<StudentResponse>> studentResponsesOptional =
                studentService.getStudentsByCourseCount(1L);

        // Assert
        assertTrue(studentResponsesOptional.isPresent());
        StudentResponse student = studentResponsesOptional.get().get(0);
        log.info("Student : " + student.toString());
        assertEquals(correctStudentWithCourse.getId(), student.getId());
        assertEquals(correctStudentWithCourse.getFirstName(), student.getFirstName());
        assertEquals(correctStudentWithCourse.getLastName(), student.getLastName());
        assertNull(student.getPatronymic());
        assertEquals(correctStudentWithCourse.getEmail(), student.getEmail());
        assertEquals(courseName, student.getCoursesNames().get(0));
        log.info("getStudentsByCourseCount test has successfully finished.");
    }
}
