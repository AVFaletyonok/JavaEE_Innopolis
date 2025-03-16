package org.example;

import org.example.model.Course;
import org.example.model.Student;
import org.example.repository.StudentRepository;
import org.example.service.CourseService;
import org.example.service.StudentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = StudentService.class)
public class StudentServiceTest {

    @Autowired
    private StudentService studentService;
    @MockitoBean
    private StudentRepository studentRepository;
    @MockitoBean
    private CourseService courseService;
    private final Student correctStudent = new Student(1L, "Ivan", "Ivanov",
                                            null, "ivan@mail.ru", null);

    @Test
    public void isEmailCorrect() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final String correctEmail = "anton@mail.ru";
        assertTrue(StudentService.isEmailCorrect(correctEmail));
    }

    @Test
    public void isEmailWrong() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final String wrongEmail = "a@mail.ru";
        assertFalse(StudentService.isEmailCorrect(wrongEmail));
    }

    @Test
    public void isStudentCorrect() {
        Student correctStudent = new Student(1L, "Ivan", "Ivanov",
                                    null, "ivan@mail.ru", null);
        assertTrue(studentService.isStudentCorrect(correctStudent));
    }

    @Test
    public void isStudentWrong() {
        Student wrongStudent1 = new Student(1L, null, "Ivanov",
                null, "ivan@mail.ru", null);
        Student wrongStudent2 = new Student(1L, "Ivan", null,
                null, "ivan@mail.ru", null);
        Student wrongStudent3 = new Student(1L, "Ivan", "Ivanov",
                null, null, null);
        Student wrongStudent4 = new Student(1L, "Ivan", null,
                null, "i@mail.ru", null);
        assertFalse(studentService.isStudentCorrect(wrongStudent1));
        assertFalse(studentService.isStudentCorrect(wrongStudent2));
        assertFalse(studentService.isStudentCorrect(wrongStudent3));
        assertFalse(studentService.isStudentCorrect(wrongStudent4));
    }

    @Test
    public void signUpCourseCorrect() {
        Course correctCourse = new Course(4L, "C");

        Mockito.when(studentRepository.findById(correctStudent.getId()))
                .thenReturn(Optional.of(correctStudent));
        Mockito.when(courseService.containsCourse(correctCourse.getName()))
                .thenReturn(true);
        Mockito.when(courseService.getCourseByName(correctCourse.getName()))
                .thenReturn(Optional.of(correctCourse));
        Mockito.when(studentRepository.saveAndFlush(correctStudent))
                .thenReturn(correctStudent);

        Optional<Student> actualStudentOptional = studentService.signUpCourse(correctStudent.getId(), correctCourse.getName());
        assertTrue(actualStudentOptional.isPresent());
        Student actualStudent = actualStudentOptional.get();
        assertNotNull(actualStudent.getCourses());
        assertEquals(1, actualStudent.getCourses().size());
        assertEquals(4L, (long)actualStudent.getCourses().get(0));
    }

    @Test
    public void signUpCourseWrong() {
        Course wrongCourse = new Course(6L, "Scala");

        Mockito.when(studentRepository.findById(correctStudent.getId()))
                .thenReturn(Optional.of(correctStudent));
        Mockito.when(courseService.containsCourse(wrongCourse.getName()))
                .thenReturn(false);
        Mockito.when(courseService.getCourseByName(wrongCourse.getName()))
                .thenReturn(Optional.empty());

        Optional<Student> actualStudentOptional = studentService.signUpCourse(correctStudent.getId(), wrongCourse.getName());
        assertTrue(actualStudentOptional.isEmpty());
    }
}
