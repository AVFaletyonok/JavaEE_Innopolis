import org.example.controller.RegistrationController;
import org.example.dto.student.StudentResponse;
import org.example.entity.StudentEntity;
import org.example.service.StudentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = RegistrationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = RegistrationController.class)
public class RegistrationControllerTest {

    @Autowired
    private RegistrationController registrationController;
    @MockBean
    private StudentService studentService;

    @Test
    void whenValidInput_thenReturns200AndStudentResponse() {
        StudentEntity validRequest = new StudentEntity();
        validRequest.setPassword("password123");
        validRequest.setPasswordConfirm("password123");
        StudentResponse mockResponse = new StudentResponse();

        when(studentService.createStudent(any(StudentEntity.class)))
                .thenReturn(Optional.of(mockResponse));

        ResponseEntity<StudentResponse> response = registrationController.addUser(validRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void whenPasswordsDontMatch_thenReturns400() {
        StudentEntity invalidRequest = new StudentEntity();
        invalidRequest.setPassword("password123");
        invalidRequest.setPasswordConfirm("differentPassword");

        ResponseEntity<StudentResponse> response = registrationController.addUser(invalidRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void whenServiceReturnsEmpty_thenReturns400() {
        StudentEntity validRequest = new StudentEntity();
        validRequest.setPassword("password123");
        validRequest.setPasswordConfirm("password123");

        when(studentService.createStudent(any(StudentEntity.class)))
                .thenReturn(Optional.empty());

        ResponseEntity<StudentResponse> response = registrationController.addUser(validRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }
}
