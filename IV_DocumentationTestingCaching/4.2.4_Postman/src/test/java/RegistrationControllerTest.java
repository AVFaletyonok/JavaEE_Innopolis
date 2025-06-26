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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//@WebMvcTest(controllers = RegistrationController.class)
//@AutoConfigureMockMvc(addFilters = false)
//@ContextConfiguration(classes = RegistrationController.class)
//public class RegistrationControllerTest {
//
//    @Autowired
//    private RegistrationController registrationController;
//    @MockBean
//    private StudentService studentService;
//    @Mock
//    private Model model;
//    @Mock
//    private BindingResult bindingResult;
//
//    @Test
//    void registration_ShouldReturnRegistrationViewAndAddUserFormToModel() {
//        String viewName = registrationController.registration(model);
//
//        assertEquals("registration", viewName);
//        verify(model).addAttribute(eq("userForm"), any(StudentEntity.class));
//    }
//
//    @Test
//    void addUser_WithValidStudent_ShouldCreateStudentAndReturnRegistrationViewWithSuccess() {
//        StudentEntity student = new StudentEntity();
//        student.setPassword("password");
//        student.setPasswordConfirm("password");
//        StudentResponse studentResponse =
//                new StudentResponse(1L, "John", "Doe", null,
//                                    LocalDate.now(), "john@mail.ru", null, null);
//
//        when(bindingResult.hasErrors()).thenReturn(false);
//        when(studentService.createStudent(student)).thenReturn(Optional.of(studentResponse));
//
//        String viewName = registrationController.addUser(student, bindingResult, model);
//
//        assertEquals("registration", viewName);
//        verify(model).addAttribute("registrationSuccess", true);
//        verify(model).addAttribute("studentId", studentResponse.getId());
//        verify(studentService).createStudent(student);
//    }
//}
