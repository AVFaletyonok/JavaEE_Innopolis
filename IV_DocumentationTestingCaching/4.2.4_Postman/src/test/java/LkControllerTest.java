import org.example.controller.LkController;
import org.example.dto.student.StudentResponse;
import org.example.entity.StudentEntity;
import org.example.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LkController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = LkController.class)
public class LkControllerTest {

    @Autowired
    private LkController lkController;
    @MockBean
    private StudentServiceImpl studentService;
    @Autowired
    private MockMvc mvc;

    private final StudentEntity correctStudent =
            new StudentEntity(1L, "Anton", "Petrov", "Ivanovich",
                    LocalDate.of(1985, 10, 10),
                    "anton@mail.ru", "123456", "123456", null, null);
    private final StudentResponse correctStudentResponse =
            new StudentResponse(correctStudent.getId(), correctStudent.getFirstName(),
                    correctStudent.getLastName(), correctStudent.getPatronymic(),
                    correctStudent.getBirthDate(), correctStudent.getEmail(),
                    null, null);

    @Test
    void getCorrectStudent() throws Exception {
        Mockito.when(studentService.getStudent(correctStudent.getId()))
                .thenReturn(Optional.of(correctStudentResponse));
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/students/lk/" + correctStudent.getId()))
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
    void getWrongStudent() throws Exception {
        Mockito.when(studentService.getStudent(2L))
                .thenReturn(Optional.empty());
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/students/lk/2"))
                .andExpect(status().isNotFound());
    }
}
