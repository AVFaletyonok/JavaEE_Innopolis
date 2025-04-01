package org.example;

import org.example.client.CoursesClient;
import org.example.dto.courses.CoursesResponse;
import org.example.dto.student.StudentResponse;
import org.example.entity.Student;
import org.example.repository.StudentRepository;
import org.example.service.StudentService;
import org.example.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

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
    private Student correctStudent = new Student(1L, "Ivan", "Ivanov",
                                            null, "i_ivanov@mail.ru", List.of(3L));
    private final List<CoursesResponse> coursesResponseList =
            List.of(new CoursesResponse(1L, "JavaEE", true),
                    new CoursesResponse(2L, "Python", true),
                    new CoursesResponse(3L, "Csharp", true),
                    new CoursesResponse(4L, "C", true),
                    new CoursesResponse(5L, "C++", false));

    private final Student wrongStudent = new Student(10L, null, null,
                                                null, null, null);

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
        assertEquals(coursesResponseList.get((int)correctStudent.getCourses().getFirst().longValue() - 1).getName(),
                    student.getCoursesNames().getFirst());
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
        assertEquals(coursesResponseList.get((int)correctStudent.getCourses().getFirst().longValue() - 1).getName(),
                student.getCoursesNames().getFirst());
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
        assertEquals(coursesResponseList.get((int)correctStudent.getCourses().getFirst().longValue() - 1).getName(),
                student.getCoursesNames().getFirst());
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
        assertEquals(coursesResponseList.get((int)correctStudent.getCourses().getFirst().longValue() - 1).getName(),
                student.getCoursesNames().getFirst());
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
        assertEquals(coursesResponseList.get((int)correctStudent.getCourses().getFirst().longValue() - 1).getName(),
                student.getCoursesNames().getFirst());
    }

    @Test
    public void signUpCourseCorrect() {
        Mockito.when(studentRepository.findById(correctStudent.getId()))
                .thenReturn(Optional.of(correctStudent));
        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);
        List<Long> courses = new ArrayList<>(correctStudent.getCourses());
        courses.add(4L);
        correctStudent.setCourses(courses);
        Mockito.when(studentRepository.saveAndFlush(correctStudent))
                .thenReturn(correctStudent);

        Optional<StudentResponse> actualStudentOptional =
                studentService.signUpCourse(correctStudent.getId(), coursesResponseList.get(4).getName());
        assertTrue(actualStudentOptional.isPresent());
        StudentResponse student = actualStudentOptional.get();
        assertEquals(correctStudent.getId(), student.getId());
        assertEquals(correctStudent.getFirstName(), student.getFirstName());
        assertEquals(correctStudent.getLastName(), student.getLastName());
        assertNull(student.getPatronymic());
        assertEquals(correctStudent.getEmail(), student.getEmail());
        assertEquals(coursesResponseList.get((int)correctStudent.getCourses().getFirst().longValue() - 1).getName(),
                student.getCoursesNames().getFirst());
        assertEquals(coursesResponseList.get(4).getName(), student.getCoursesNames().getLast());
    }

    @Test
    public void signUpCourseWrong() {
        Mockito.when(studentRepository.findById(correctStudent.getId()))
                .thenReturn(Optional.of(correctStudent));
        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);

        Optional<StudentResponse> actualStudentOptional =
                studentService.signUpCourse(correctStudent.getId(), "Scala");
        assertTrue(actualStudentOptional.isEmpty());
    }
}
