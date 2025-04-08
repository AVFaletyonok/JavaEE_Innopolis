package org.example;

import org.example.controller.StudentController;
import org.example.dto.student.StudentResponse;
import org.example.entity.Student;
import org.example.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
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
    private StudentServiceImpl studentService;
    @Autowired
    private MockMvc mvc;

    private final Student correctStudent = new Student(1L, "Anton", "Petrov",
                                            "Ivanovich", "anton@mail.ru", null);
    private final StudentResponse correctStudentResponse =
            new StudentResponse(correctStudent.getId(), correctStudent.getFirstName(),
                                correctStudent.getLastName(), correctStudent.getPatronymic(),
                                correctStudent.getEmail(), null);
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

    @Test
    void getCorrectStudent() throws Exception {
        Mockito.when(studentService.getStudent(correctStudent.getId()))
                .thenReturn(Optional.of(correctStudentResponse));
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(correctStudent.getId()))
                .andExpect(jsonPath("$.firstName").value(correctStudent.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(correctStudent.getLastName()))
                .andExpect(jsonPath("$.patronymic").value(correctStudent.getPatronymic()))
                .andExpect(jsonPath("$.email").value(correctStudent.getEmail()))
                .andExpect(jsonPath("$.courses").doesNotExist());
    }

    @Test
    void getWrongStudent() throws Exception {
        Mockito.when(studentService.getStudent(2L))
                .thenReturn(Optional.empty());
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/students/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCorrectStudent() throws Exception {
        Mockito.when(studentService.createStudent(correctStudent))
                .thenReturn(Optional.of(correctStudentResponse));
        mvc.perform(MockMvcRequestBuilders.post("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(correctStudentJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(correctStudent.getId()))
                .andExpect(jsonPath("$.firstName").value(correctStudent.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(correctStudent.getLastName()))
                .andExpect(jsonPath("$.patronymic").value(correctStudent.getPatronymic()))
                .andExpect(jsonPath("$.email").value(correctStudent.getEmail()))
                .andExpect(jsonPath("$.courses").doesNotExist());
    }

    @Test
    void createWrongStudent() throws Exception {
        Mockito.when(studentService.createStudent(wrongStudent))
                .thenReturn(Optional.empty());
        mvc.perform(MockMvcRequestBuilders.post("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(wrongStudentJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateCorrectStudent() throws Exception {
        Mockito.when(studentService.updateStudent(correctStudent))
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
                .andExpect(jsonPath("$.courses").doesNotExist());
    }

    @Test
    void updateWrongStudent() throws Exception {
        Mockito.when(studentService.updateStudent(wrongStudent))
                .thenReturn(Optional.empty());
        mvc.perform(MockMvcRequestBuilders.put("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(wrongStudentJson))
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
                .andExpect(jsonPath("$.courses").doesNotExist());
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
                .andExpect(jsonPath("$.[0].courses").doesNotExist());
    }

    @Test
    void signUpForCorrectCourse() throws Exception {
        final Student correctStudentWithCourse =
                new Student(correctStudent.getId(), correctStudent.getFirstName(),
                            correctStudent.getLastName(), correctStudent.getPatronymic(),
                            correctStudent.getEmail(), List.of(3L));
        final StudentResponse correctStudentResponseWithCourses =
                new StudentResponse(correctStudent.getId(), correctStudent.getFirstName(),
                                    correctStudent.getLastName(), correctStudent.getPatronymic(),
                                    correctStudent.getEmail(), List.of("Csharp", "C"));
        final String courseName = "C";
        Mockito.when(studentService.signUpCourse(correctStudentWithCourse.getId(), courseName))
                        .thenReturn(Optional.of(correctStudentResponseWithCourses));
        mvc.perform(MockMvcRequestBuilders
                .post("/api/v1/students/" + correctStudent.getId() + "/courses/" + courseName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(correctStudent.getId()))
                .andExpect(jsonPath("$.firstName").value(correctStudent.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(correctStudent.getLastName()))
                .andExpect(jsonPath("$.patronymic").value(correctStudent.getPatronymic()))
                .andExpect(jsonPath("$.email").value(correctStudent.getEmail()))
                .andExpect(jsonPath("$.coursesNames[0]").value(correctStudentResponseWithCourses.getCoursesNames().get(0)))
                .andExpect(jsonPath("$.coursesNames[1]").value(correctStudentResponseWithCourses.getCoursesNames().get(1)));
    }

    @Test
    void signUpForWrongCourse() throws Exception {
        final String wrongCourseName = "Scala";
        Mockito.when(studentService.signUpCourse(correctStudent.getId(), wrongCourseName))
                .thenReturn(Optional.empty());
        mvc.perform(MockMvcRequestBuilders
                .post("/api/v1/students/" + correctStudent.getId() + "/courses/" + wrongCourseName))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getStudentsByCorrectCourse() throws Exception {
        final String correctCourseName = "C";
        StudentResponse student1 = new StudentResponse(1L, "Ivan", "Vasil'ev", null,
                "i_vasilev@mail.ru", new ArrayList<>(List.of(correctCourseName)));
        StudentResponse student2 = new StudentResponse(3L, "Oleg", "Ivanov", null,
                "o_ivanov@mail.ru", new ArrayList<>(List.of(correctCourseName)));

        Mockito.when(studentService.getStudentsByCourse(correctCourseName))
                .thenReturn(Optional.of(List.of(student1, student2)));
        mvc.perform((MockMvcRequestBuilders
                .get("/api/v1/students/oncourse=" + correctCourseName)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(student1.getId()))
                .andExpect(jsonPath("$.[0].firstName").value(student1.getFirstName()))
                .andExpect(jsonPath("$.[0].lastName").value(student1.getLastName()))
                .andExpect(jsonPath("$.[0].patronymic").value(student1.getPatronymic()))
                .andExpect(jsonPath("$.[0].email").value(student1.getEmail()))
                .andExpect(jsonPath("$.[0].coursesNames").exists())
                .andExpect(jsonPath("$.[0].coursesNames.[0]").value(student1.getCoursesNames().getFirst()))

                .andExpect(jsonPath("$.[1].id").value(student2.getId()))
                .andExpect(jsonPath("$.[1].firstName").value(student2.getFirstName()))
                .andExpect(jsonPath("$.[1].lastName").value(student2.getLastName()))
                .andExpect(jsonPath("$.[1].patronymic").value(student2.getPatronymic()))
                .andExpect(jsonPath("$.[1].email").value(student2.getEmail()))
                .andExpect(jsonPath("$.[1].coursesNames").exists())
                .andExpect(jsonPath("$.[1].coursesNames.[0]").value(student2.getCoursesNames().getFirst()));
    }

    @Test
    void getStudentByWrongCourse() throws Exception {
        final String wrongCourseName = "Scala";
        Mockito.when(studentService.getStudentsByCourse(wrongCourseName))
                .thenReturn(Optional.empty());
        mvc.perform((MockMvcRequestBuilders
                        .get("/api/v1/students/oncourse=" + wrongCourseName)))
                .andExpect(status().isBadRequest());
    }
}
