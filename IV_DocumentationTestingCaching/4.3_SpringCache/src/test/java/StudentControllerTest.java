import org.example.controller.StudentController;
import org.example.dto.review.ReviewResponse;
import org.example.dto.student.StudentResponse;
import org.example.entity.CoursesEntity;
import org.example.entity.ReviewEntity;
import org.example.entity.StudentEntity;
import org.example.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StudentController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private StudentController studentController;

    @MockBean
    private StudentServiceImpl studentService;

    private final StudentEntity correctStudent =
            new StudentEntity(1L, "Anton", "Petrov", "Ivanovich",
                                            LocalDate.of(1985, 10, 10),
                                            "anton@mail.ru", "123456", "123456",
                                            null, null);
    private final StudentResponse correctStudentResponse =
            new StudentResponse(correctStudent.getId(), correctStudent.getFirstName(),
                                correctStudent.getLastName(), correctStudent.getPatronymic(),
                                correctStudent.getBirthDate(), correctStudent.getEmail(),
                                null, null);
    private final String correctStudentJson = "{"
                                            + "\"id\":1,"
                                            + "\"firstName\":\"Anton\","
                                            + "\"lastName\":\"Petrov\","
                                            + "\"patronymic\":\"Ivanovich\","
                                            + "\"birthDate\":\"1985-10-10\","
                                            + "\"email\":\"anton@mail.ru\","
                                            + "\"studentGroup\":{"
                                            + "\"name\":\"Java_programmers\"},"
                                            + "\"courses\":null"
                                            + "}";
    private final StudentEntity wrongStudent =
            new StudentEntity(2L, null, "Petrov", "Ivanovich",
                    null, null, null, null, null, null);
    private final String wrongStudentJson = "{"
                                            + "\"id\":2,"
                                            + "\"firstName\":null,"
                                            + "\"lastName\":\"Petrov\","
                                            + "\"patronymic\":\"Ivanovich\"";

    private final CoursesEntity course =
            new CoursesEntity(1L, 2L, new ArrayList<>(), new ArrayList<>());
    private final String courseName = "Python";
    private final StudentEntity correctStudentWithCourse =
            new StudentEntity(correctStudent.getId(), correctStudent.getFirstName(),
                    correctStudent.getLastName(), correctStudent.getPatronymic(),
                    correctStudent.getBirthDate(), correctStudent.getEmail(),
                    correctStudent.getPassword(), correctStudent.getPasswordConfirm(),
                    new ArrayList<>(), new ArrayList<>(List.of(course)));
    private final StudentResponse correctStudentResponseWithCourses =
            new StudentResponse(correctStudent.getId(), correctStudent.getFirstName(),
                    correctStudent.getLastName(), correctStudent.getPatronymic(),
                    correctStudent.getBirthDate(), correctStudent.getEmail(),
                    new ArrayList<>(List.of(courseName)), new ArrayList<>());

    @Test
//    @WithMockUser(username = "admin", roles = "ADMIN")
    void updateCorrectStudent() throws Exception {
        Mockito.when(studentService.updateStudent(
                Mockito.argThat(student ->
                        student.getFirstName().equals(correctStudent.getFirstName()) &&
                        student.getLastName().equals(correctStudent.getLastName()) &&
                        student.getPatronymic().equals(correctStudent.getPatronymic()) &&
                        student.getEmail().equals(correctStudent.getEmail()))))
                .thenReturn(Optional.of(correctStudentResponse));
        mvc.perform(MockMvcRequestBuilders.put("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(correctStudentJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(correctStudent.getId()))
                .andExpect(jsonPath("$.firstName").value(correctStudent.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(correctStudent.getLastName()))
                .andExpect(jsonPath("$.patronymic").value(correctStudent.getPatronymic()))
                .andExpect(jsonPath("$.email").value(correctStudent.getEmail()))
                .andExpect(jsonPath("$.coursesNames").doesNotExist())
                .andExpect(jsonPath("$.reviews").doesNotExist());
    }

    @Test
    void updateWrongStudent() throws Exception {
        Mockito.when(studentService.updateStudent(
                        Mockito.argThat(student ->
                                student.getFirstName().equals(correctStudent.getFirstName()) &&
                                student.getLastName().equals(correctStudent.getLastName()) &&
                                student.getPatronymic().equals(correctStudent.getPatronymic()) &&
                                student.getEmail().equals(correctStudent.getEmail()))))
                .thenReturn(Optional.empty());
        mvc.perform(MockMvcRequestBuilders.put("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(correctStudentJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCorrectStudent() throws Exception {
        Mockito.when(studentService.deleteStudent(correctStudent.getId()))
                .thenReturn(Optional.of(correctStudentResponse));
        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/students/" + correctStudent.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(correctStudent.getId()))
                .andExpect(jsonPath("$.firstName").value(correctStudent.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(correctStudent.getLastName()))
                .andExpect(jsonPath("$.patronymic").value(correctStudent.getPatronymic()))
                .andExpect(jsonPath("$.email").value(correctStudent.getEmail()))
                .andExpect(jsonPath("$.coursesNames").doesNotExist())
                .andExpect(jsonPath("$.reviews").doesNotExist());
    }

    @Test
    void deleteWrongStudent() throws Exception {
        Mockito.when(studentService.deleteStudent(wrongStudent.getId()))
                .thenReturn(Optional.empty());
        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/students/" + wrongStudent.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getStudents() throws Exception {
        Mockito.when(studentService.getStudents())
                .thenReturn(List.of(correctStudentResponse));
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(correctStudent.getId()))
                .andExpect(jsonPath("$.[0].firstName").value(correctStudent.getFirstName()))
                .andExpect(jsonPath("$.[0].lastName").value(correctStudent.getLastName()))
                .andExpect(jsonPath("$.[0].patronymic").value(correctStudent.getPatronymic()))
                .andExpect(jsonPath("$.[0].email").value(correctStudent.getEmail()))
                .andExpect(jsonPath("$.[0].coursesNames").doesNotExist())
                .andExpect(jsonPath("$.[0].reviews").doesNotExist());
    }

    /*
    "JavaEE",
    "Python",
    "Csharp",
    "C",
    "C++"
    */
    @Test
    void signUpForCorrectCourse() throws Exception {
        course.getStudents().add(correctStudentWithCourse);
        Mockito.when(studentService.signUpCourse(correctStudentWithCourse.getId(), courseName))
                        .thenReturn(Optional.of(correctStudentResponseWithCourses));

        mvc.perform(MockMvcRequestBuilders
                .post("/api/v1/students/" + correctStudent.getId() + "/courses/signup/" + courseName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(correctStudent.getId()))
                .andExpect(jsonPath("$.firstName").value(correctStudent.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(correctStudent.getLastName()))
                .andExpect(jsonPath("$.patronymic").value(correctStudent.getPatronymic()))
                .andExpect(jsonPath("$.email").value(correctStudent.getEmail()))
                .andExpect(jsonPath("$.coursesNames[0]")
                        .value(correctStudentResponseWithCourses.getCoursesNames().get(0)));
    }

    @Test
    void signUpForWrongCourse() throws Exception {
        final String wrongCourseName = "Scala";
        Mockito.when(studentService.signUpCourse(correctStudent.getId(), wrongCourseName))
                .thenReturn(Optional.empty());
        mvc.perform(MockMvcRequestBuilders
                .post("/api/v1/students/" + correctStudent.getId() + "/courses/signup/" + wrongCourseName))
                .andExpect(status().isBadRequest());
    }

    @Test
    void leaveReviewOnCorrectCourse() throws Exception {
        String reviewText = "great";
        ReviewEntity review = new ReviewEntity(1L, reviewText, correctStudentWithCourse, course);
        course.getReviews().add(review);
        correctStudentWithCourse.getReviews().add(review);
        ReviewResponse reviewResponse = new ReviewResponse(courseName, reviewText);
        correctStudentResponseWithCourses.getReviews().add(reviewResponse);

        Mockito.when(studentService.leaveReviewOnCourse(correctStudentWithCourse.getId(), courseName, reviewText))
                .thenReturn(Optional.of(correctStudentResponseWithCourses));

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/students/" + correctStudentWithCourse.getId()
                                + "/courses/leave-review/" + courseName)
                .content(reviewText)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(correctStudent.getId()))
                .andExpect(jsonPath("$.firstName").value(correctStudent.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(correctStudent.getLastName()))
                .andExpect(jsonPath("$.patronymic").value(correctStudent.getPatronymic()))
                .andExpect(jsonPath("$.email").value(correctStudent.getEmail()))
                .andExpect(jsonPath("$.coursesNames[0]")
                        .value(correctStudentResponseWithCourses.getCoursesNames().get(0)))
                .andExpect(jsonPath("$.reviews[0].courseName").value(courseName))
                .andExpect(jsonPath("$.reviews[0].text").value(reviewText));
    }

    @Test
    void leaveReviewOnWrongCourse() throws Exception {
        String wrongCourseName = "C";
        Mockito.when(studentService.leaveReviewOnCourse(correctStudentWithCourse.getId(), wrongCourseName, "great"))
                .thenReturn(Optional.of(correctStudentResponseWithCourses));

        mvc.perform(MockMvcRequestBuilders
                .post("/api/v1/students/" + correctStudentWithCourse.getId()
                        + "/courses/leave-review/" + wrongCourseName))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getStudentsByCorrectCourse() throws Exception {
        course.getStudents().add(correctStudentWithCourse);

        Mockito.when(studentService.getStudentsByCourse(courseName))
                .thenReturn(Optional.of(List.of(correctStudentResponseWithCourses)));
        mvc.perform((MockMvcRequestBuilders
                .get("/api/v1/students/on-course=" + courseName)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(correctStudentWithCourse.getId()))
                .andExpect(jsonPath("$.[0].firstName").value(correctStudentWithCourse.getFirstName()))
                .andExpect(jsonPath("$.[0].lastName").value(correctStudentWithCourse.getLastName()))
                .andExpect(jsonPath("$.[0].patronymic").value(correctStudentWithCourse.getPatronymic()))
                .andExpect(jsonPath("$.[0].email").value(correctStudentWithCourse.getEmail()))
                .andExpect(jsonPath("$.[0].coursesNames").exists())
                .andExpect(jsonPath("$.[0].coursesNames.[0]")
                        .value(correctStudentResponseWithCourses.getCoursesNames().get(0)));
    }

    @Test
    void getStudentByWrongCourse() throws Exception {
        final String wrongCourseName = "Scala";
        Mockito.when(studentService.getStudentsByCourse(wrongCourseName))
                .thenReturn(Optional.empty());
        mvc.perform((MockMvcRequestBuilders
                        .get("/api/v1/students/on-course=" + wrongCourseName)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getStudentsOlderThan30() throws Exception {
        Mockito.when(studentService.getStudentsOlderThan(30))
                .thenReturn(Optional.of(List.of(correctStudentResponse)));
        mvc.perform((MockMvcRequestBuilders
                        .get("/api/v1/students/older-than=30")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(correctStudent.getId()))
                .andExpect(jsonPath("$.[0].firstName").value(correctStudent.getFirstName()))
                .andExpect(jsonPath("$.[0].lastName").value(correctStudent.getLastName()))
                .andExpect(jsonPath("$.[0].patronymic").value(correctStudent.getPatronymic()))
                .andExpect(jsonPath("$.[0].email").value(correctStudent.getEmail()))
                .andExpect(jsonPath("$.[0].coursesNames").doesNotExist());
    }

    @Test
    void getStudentsOlderThanWrong() throws Exception {
        Mockito.when(studentService.getStudentsOlderThan(150))
                .thenReturn(Optional.empty());
        mvc.perform((MockMvcRequestBuilders
                        .get("/api/v1/students/older-than=150")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getStudentsByNameAndCourseName() throws Exception {
        Mockito.when(studentService.getStudentsByNameAndCourseName(correctStudentWithCourse.getFirstName(), "Python"))
                .thenReturn(Optional.of(List.of(correctStudentResponseWithCourses)));
        mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/students/name=" + correctStudent.getFirstName() + "/course=Python"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(correctStudent.getId()))
                .andExpect(jsonPath("$.[0].firstName").value(correctStudent.getFirstName()))
                .andExpect(jsonPath("$.[0].lastName").value(correctStudent.getLastName()))
                .andExpect(jsonPath("$.[0].patronymic").value(correctStudent.getPatronymic()))
                .andExpect(jsonPath("$.[0].email").value(correctStudent.getEmail()))
                .andExpect(jsonPath("$.[0].coursesNames[0]")
                        .value(correctStudentResponseWithCourses.getCoursesNames().get(0)));
    }

    @Test
    void getStudentsByMinimumCourses() throws Exception {
        Mockito.when(studentService.getStudentsByCourseCount(1L))
                .thenReturn(Optional.of(List.of(correctStudentResponseWithCourses)));
        mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/students/course-count=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(correctStudent.getId()))
                .andExpect(jsonPath("$.[0].firstName").value(correctStudent.getFirstName()))
                .andExpect(jsonPath("$.[0].lastName").value(correctStudent.getLastName()))
                .andExpect(jsonPath("$.[0].patronymic").value(correctStudent.getPatronymic()))
                .andExpect(jsonPath("$.[0].email").value(correctStudent.getEmail()))
                .andExpect(jsonPath("$.[0].coursesNames[0]")
                        .value(correctStudentResponseWithCourses.getCoursesNames().get(0)));
    }
}
