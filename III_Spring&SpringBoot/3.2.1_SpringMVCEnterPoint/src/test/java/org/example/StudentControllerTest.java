package org.example;

import org.example.controller.StudentController;
import org.example.model.Student;
import org.example.repository.StudentRepository;
import org.example.service.CourseService;
import org.example.service.StudentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StudentController.class)
@ContextConfiguration(classes = StudentController.class)
public class StudentControllerTest {

    @Autowired
    private StudentController studentController;
    @MockitoBean
    private StudentRepository studentRepository;
    @MockitoBean
    private StudentService studentService;
    @MockitoBean
    private CourseService courseService;
    @Autowired
    private MockMvc mvc;

    private final Student correctStudent = new Student(1L, "Anton", "Petrov",
                                            "Ivanovich", "anton@mail.ru", null);
    private final String correctStudentJson = "{"
                                            + "\"id\":1,"
                                            + "\"firstName\":\"Anton\","
                                            + "\"lastName\":\"Petrov\","
                                            + "\"patronymic\":\"Ivanovich\","
                                            + "\"email\":\"anton@mail.ru\","
                                            + "\"courses\":null"
                                            + "}";
    private final Student wrongStudent = new Student(2L, null, "Petrov",
                                            "Ivanovich", "anton@mail.ru", null);
    private final String wrongStudentJson = "{"
                                            + "\"id\":2,"
                                            + "\"firstName\":null,"
                                            + "\"lastName\":\"Petrov\","
                                            + "\"patronymic\":\"Ivanovich\","
                                            + "\"email\":\"anton@mail.ru\","
                                            + "\"courses\":null"
                                            + "}";
    private final Student updateCorrectStudent = new Student(1L, "Ivan", "Pavlov",
                                                "Sergeevich", "ivan@mail.ru", null);
    private final String updateCorrectStudentJson = "{"
                                                + "\"id\":1,"
                                                + "\"firstName\":\"Ivan\","
                                                + "\"lastName\":\"Pavlov\","
                                                + "\"patronymic\":\"Sergeevich\","
                                                + "\"email\":\"ivan@mail.ru\","
                                                + "\"courses\":null"
                                                + "}";
    private final Student updateWrongStudent = new Student(2L, "Olga", "Vasil'eva",
                                                "Sergeevna", "olga@mail.ru", null);
    private final String updateWrongStudentJson = "{"
                                                + "\"id\":2,"
                                                + "\"firstName\":\"Olga\","
                                                + "\"lastName\":\"Vasil'eva\","
                                                + "\"patronymic\":\"Sergeevna\","
                                                + "\"email\":\"olga@mail.ru\","
                                                + "\"courses\":null"
                                                + "}";

    @Test
    void getCorrectStudent() throws Exception {

        Mockito.when(studentRepository.findById(1L))
                .thenReturn(Optional.of(correctStudent));

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("Anton"))
                .andExpect(jsonPath("$.lastName").value("Petrov"))
                .andExpect(jsonPath("$.patronymic").value("Ivanovich"))
                .andExpect(jsonPath("$.email").value("anton@mail.ru"))
                .andExpect(jsonPath("$.courses").doesNotExist());
    }

    @Test
    void getWrongStudent() throws Exception {
        Mockito.when(studentRepository.findById(2L))
                .thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/students/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCorrectStudent() throws Exception {

        Mockito.when(studentService.isStudentCorrect(correctStudent))
                .thenReturn(true);
        Mockito.when(studentRepository.saveAndFlush(correctStudent))
                .thenReturn(correctStudent);

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(correctStudentJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("Anton"))
                .andExpect(jsonPath("$.lastName").value("Petrov"))
                .andExpect(jsonPath("$.patronymic").value("Ivanovich"))
                .andExpect(jsonPath("$.email").value("anton@mail.ru"))
                .andExpect(jsonPath("$.courses").doesNotExist());
    }

    @Test
    void createWrongStudent() throws Exception {
        Mockito.when(studentService.isStudentCorrect(wrongStudent))
                .thenReturn(false);

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(wrongStudentJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateCorrectStudent() throws Exception {
        Mockito.when(studentService.isStudentCorrect(updateCorrectStudent))
                .thenReturn(true);
        Mockito.when(studentRepository.findById(updateCorrectStudent.getId()))
                .thenReturn(Optional.of(correctStudent));
        Mockito.when(studentRepository.saveAndFlush(updateCorrectStudent))
                .thenReturn(updateCorrectStudent);

        mvc.perform(MockMvcRequestBuilders.put("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateCorrectStudentJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("Ivan"))
                .andExpect(jsonPath("$.lastName").value("Pavlov"))
                .andExpect(jsonPath("$.patronymic").value("Sergeevich"))
                .andExpect(jsonPath("$.email").value("ivan@mail.ru"))
                .andExpect(jsonPath("$.courses").doesNotExist());
    }

    @Test
    void updateWrongStudent() throws Exception {
        Mockito.when(studentService.isStudentCorrect(updateWrongStudent))
                .thenReturn(true);
        Mockito.when(studentRepository.findById(updateWrongStudent.getId()))
                .thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.put("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateWrongStudentJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCorrectStudent() throws Exception {
        Mockito.when(studentRepository.findById(correctStudent.getId()))
                .thenReturn(Optional.of(correctStudent));

        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/students/" + correctStudent.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("Anton"))
                .andExpect(jsonPath("$.lastName").value("Petrov"))
                .andExpect(jsonPath("$.patronymic").value("Ivanovich"))
                .andExpect(jsonPath("$.email").value("anton@mail.ru"))
                .andExpect(jsonPath("$.courses").doesNotExist());
    }

    @Test
    void deleteWrongStudent() throws Exception {
        Mockito.when(studentRepository.findById(wrongStudent.getId()))
                .thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/students/" + wrongStudent.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getStudents() throws Exception {
        Mockito.when(studentRepository.findAll())
                .thenReturn(List.of(correctStudent, updateWrongStudent));

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].firstName").value("Anton"))
                .andExpect(jsonPath("$.[0].lastName").value("Petrov"))
                .andExpect(jsonPath("$.[0].patronymic").value("Ivanovich"))
                .andExpect(jsonPath("$.[0].email").value("anton@mail.ru"))
                .andExpect(jsonPath("$.[0].courses").doesNotExist())
                .andExpect(jsonPath("$.[1].id").value(2L))
                .andExpect(jsonPath("$.[1].firstName").value("Olga"))
                .andExpect(jsonPath("$.[1].lastName").value("Vasil'eva"))
                .andExpect(jsonPath("$.[1].patronymic").value("Sergeevna"))
                .andExpect(jsonPath("$.[1].email").value("olga@mail.ru"))
                .andExpect(jsonPath("$.[1].courses").doesNotExist());
    }

    @Test
    void signUpForCorrectCourse() throws Exception {
        final Student correctStudentWithCourse = new Student(1L, "Anton", "Petrov",
                                                "Ivanovich", "anton@mail.ru", List.of(4L));
        final String courseName = "C";
        Mockito.when(studentService.signUpCourse(correctStudentWithCourse.getId(), courseName))
                        .thenReturn(Optional.of(correctStudentWithCourse));

        mvc.perform(MockMvcRequestBuilders
                .post("/api/v1/students/" + correctStudent.getId() + "/courses/" + courseName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("Anton"))
                .andExpect(jsonPath("$.lastName").value("Petrov"))
                .andExpect(jsonPath("$.patronymic").value("Ivanovich"))
                .andExpect(jsonPath("$.email").value("anton@mail.ru"))
                .andExpect(jsonPath("$.courses[0]").value(4L));
    }

    @Test
    void signUpForWrongCourse() throws Exception {
        final String courseName = "Scala";
        Mockito.when(studentService.signUpCourse(correctStudent.getId(), courseName))
                .thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/students/" + correctStudent.getId() + "/courses/" + courseName))
                .andExpect(status().isBadRequest());
    }
}
