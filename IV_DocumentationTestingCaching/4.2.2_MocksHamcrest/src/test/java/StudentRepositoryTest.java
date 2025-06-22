import org.example.entity.StudentEntity;
import org.example.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@DataJpaTest
//@ContextConfiguration(classes = StudentRepository.class)
//public class StudentRepositoryTest {
//
//    @Autowired
//    private StudentRepository studentRepository;
//
//    private StudentEntity student =
//            new StudentEntity(1L, "Alex", "Petrov", "Ivanovich",
//                    LocalDate.of(1980, 3, 3),
//                    "alex@mail.ru", "password", "password",
//                    new ArrayList<>(), new ArrayList<>());
//
//    @BeforeEach
//    void init() {
//        studentRepository.save(student);
//    }
//
//    @Test
//    void findStudentsOlderThanTest() {
//        long age = 30L;
//        LocalDate dateOfBirth = LocalDate.now().minusYears(age);
//        List<StudentEntity> studentList = studentRepository.findStudentsOlderThan(dateOfBirth);
//
//        assertNotNull(studentList);
//        assertEquals(1, studentList.size());
//        StudentEntity gottenStudent = studentList.get(0);
//        assertEquals(student.getId(), gottenStudent.getId());
//        assertEquals(student.getFirstName(), gottenStudent.getFirstName());
//        assertEquals(student.getLastName(), gottenStudent.getLastName());
//        assertEquals(student.getPatronymic(), gottenStudent.getPatronymic());
//        assertEquals(student.getBirthDate(), gottenStudent.getBirthDate());
//        assertEquals(student.getEmail(), gottenStudent.getEmail());
//        assertEquals(student.getPassword(), gottenStudent.getPassword());
//    }
//}
