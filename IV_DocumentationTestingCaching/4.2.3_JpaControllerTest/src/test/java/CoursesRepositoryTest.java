import org.example.Main;
import org.example.entity.CoursesEntity;
import org.example.repository.CoursesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ContextConfiguration(classes = Main.class)
public class CoursesRepositoryTest {

    @Autowired
    private CoursesRepository coursesRepository;

    private final CoursesEntity course =
            new CoursesEntity(1L, 2L, new ArrayList<>(), new ArrayList<>());

    @BeforeEach
    void init() {
        coursesRepository.deleteAll();
        coursesRepository.save(course);
    }

    @Test
    void findByCourseIdTest() {
        Long courseId = 2L;
        CoursesEntity gottenCourse = coursesRepository.findByCourseId(courseId);

        assertNotNull(gottenCourse);
        assertEquals(course.getId(), gottenCourse.getId());
        assertEquals(course.getCourseId(), gottenCourse.getCourseId());
    }
}
