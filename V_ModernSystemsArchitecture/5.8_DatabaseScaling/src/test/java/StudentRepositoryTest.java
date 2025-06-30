import org.example.Main;
import org.example.entity.CoursesEntity;
import org.example.entity.StudentEntity;
import org.example.repository.CoursesRepository;
import org.example.repository.StudentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = Main.class)
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CoursesRepository coursesRepository;

    private StudentEntity student =
            new StudentEntity(1L, "Alex", "Petrov", "Ivanovich",
                    LocalDate.of(1980, 3, 3),
                    "alex@mail.ru", "password", "password",
                    new ArrayList<>(), new ArrayList<>());
    private final CoursesEntity course =
            new CoursesEntity(1L, 2L, new ArrayList<>(), new ArrayList<>());

    @BeforeEach
    void init() {
        studentRepository.deleteAll();
        coursesRepository.deleteAll();
        coursesRepository.save(course);
        student.getCourses().add(course);
        course.getStudents().add(student);
        studentRepository.save(student);
        coursesRepository.save(course);
    }

    @Test
    void findStudentsOlderThanTest() {
        long age = 30L;
        LocalDate dateOfBirth = LocalDate.now().minusYears(age);
        List<StudentEntity> studentList = studentRepository.findStudentsOlderThan(dateOfBirth);

        Assertions.assertNotNull(studentList);
        Assertions.assertEquals(1, studentList.size());
        StudentEntity gottenStudent = studentList.get(0);
        Assertions.assertEquals(student.getId(), gottenStudent.getId());
        Assertions.assertEquals(student.getFirstName(), gottenStudent.getFirstName());
        Assertions.assertEquals(student.getLastName(), gottenStudent.getLastName());
        Assertions.assertEquals(student.getPatronymic(), gottenStudent.getPatronymic());
        Assertions.assertEquals(student.getBirthDate(), gottenStudent.getBirthDate());
        Assertions.assertEquals(student.getEmail(), gottenStudent.getEmail());
        Assertions.assertEquals(student.getPassword(), gottenStudent.getPassword());
    }

    @Test
    void findStudentsByCourseCountTest() {
        long courseCount = 1;
        List<StudentEntity> studentList = studentRepository.findStudentsByCourseCount(courseCount);

        Assertions.assertNotNull(studentList);
        Assertions.assertEquals(1, studentList.size());
        StudentEntity gottenStudent = studentList.get(0);
        Assertions.assertEquals(student.getId(), gottenStudent.getId());
        Assertions.assertEquals(student.getFirstName(), gottenStudent.getFirstName());
        Assertions.assertEquals(student.getLastName(), gottenStudent.getLastName());
        Assertions.assertEquals(student.getPatronymic(), gottenStudent.getPatronymic());
        Assertions.assertEquals(student.getBirthDate(), gottenStudent.getBirthDate());
        Assertions.assertEquals(student.getEmail(), gottenStudent.getEmail());
        Assertions.assertEquals(student.getPassword(), gottenStudent.getPassword());
        Assertions.assertEquals(1, gottenStudent.getCourses().size());
    }

    @Test
    void findAllByNameAndCourseIdTest() {
        String name = student.getFirstName();
        Long courseId = 2L;
        List<StudentEntity> studentList = studentRepository.findAllByNameAndCourseId(name, courseId);

        Assertions.assertNotNull(studentList);
        Assertions.assertEquals(1, studentList.size());
        StudentEntity gottenStudent = studentList.get(0);
        Assertions.assertEquals(student.getId(), gottenStudent.getId());
        Assertions.assertEquals(student.getFirstName(), gottenStudent.getFirstName());
        Assertions.assertEquals(student.getLastName(), gottenStudent.getLastName());
        Assertions.assertEquals(student.getPatronymic(), gottenStudent.getPatronymic());
        Assertions.assertEquals(student.getBirthDate(), gottenStudent.getBirthDate());
        Assertions.assertEquals(student.getEmail(), gottenStudent.getEmail());
        Assertions.assertEquals(student.getPassword(), gottenStudent.getPassword());
        Assertions.assertEquals(1, gottenStudent.getCourses().size());
    }
}
