import org.example.client.CoursesClient;
import org.example.client.StudentsClient;
import org.example.dto.course.CourseResponse;
import org.example.dto.student.StudentResponse;
import org.example.mail.MailSender;
import org.example.service.CourseMonitorService;
import org.example.service.impl.CourseMonitorServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = CourseMonitorServiceImpl.class)
public class CourseMonitorServiceTest {

    @Autowired
    private CourseMonitorService courseMonitorService;
    @MockitoBean
    private CoursesClient coursesClient;
    @MockitoBean
    private StudentsClient studentsClient;
    @MockitoBean
    private MailSender mailSender;

    private final List<CourseResponse> coursesResponseList = new ArrayList<>(
            List.of(new CourseResponse(1L, "JavaEE", new Date(), true)));

    private final List<StudentResponse> studentsResponseList = new ArrayList<>(
            List.of(new StudentResponse(1L, "Ivan", "Ivanov", null,
                    LocalDate.of(1980, 2, 21),
                    "i_ivanov@mail.ru", new ArrayList<>(List.of("JavaEE")),
                    new ArrayList<>())));

    @Test
    public void checkCourses() {
        Mockito.when(coursesClient.getCourses())
                        .thenReturn(coursesResponseList);
        Mockito.when(studentsClient.getStudentsByCourse(coursesResponseList.get(0).getName()))
                .thenReturn(studentsResponseList);

        courseMonitorService.checkCourses();
    }

    @Test
    public void getCoursesStartedOnDay() {
        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);

        Optional<List<CourseResponse>> coursesOpt = courseMonitorService.getCoursesStartedOnDay("2025-05-31");

        assertTrue(coursesOpt.isPresent());
        List<CourseResponse> courses = coursesOpt.get();
        assertEquals(1, courses.size());
        assertEquals(coursesResponseList.get(0).getId(), courses.get(0).getId());
        assertEquals(coursesResponseList.get(0).getName(), courses.get(0).getName());
        assertEquals(coursesResponseList.get(0).getStartDate(), courses.get(0).getStartDate());
        assertEquals(coursesResponseList.get(0).isActive(), courses.get(0).isActive());
    }

    @Test
    public void getCoursesStartedToday() {
        Mockito.when(coursesClient.getCourses())
                .thenReturn(coursesResponseList);

        Optional<List<CourseResponse>> coursesOpt = courseMonitorService.getCoursesStartedToday();

        assertTrue(coursesOpt.isPresent());
        List<CourseResponse> courses = coursesOpt.get();
        assertEquals(1, courses.size());
        assertEquals(coursesResponseList.get(0).getId(), courses.get(0).getId());
        assertEquals(coursesResponseList.get(0).getName(), courses.get(0).getName());
        assertEquals(coursesResponseList.get(0).getStartDate(), courses.get(0).getStartDate());
        assertEquals(coursesResponseList.get(0).isActive(), courses.get(0).isActive());
    }
}
