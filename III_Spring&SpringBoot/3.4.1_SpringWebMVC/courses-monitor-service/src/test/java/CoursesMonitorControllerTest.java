import org.example.controller.CoursesMonitorController;
import org.example.dto.course.CourseResponse;
import org.example.dto.review.ReviewResponse;
import org.example.dto.student.StudentResponse;
import org.example.service.CourseMonitorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CoursesMonitorController.class)
@ContextConfiguration(classes = CoursesMonitorController.class)
public class CoursesMonitorControllerTest {

    @Autowired
    private CoursesMonitorController coursesMonitorController;
    @MockitoBean
    private CourseMonitorService courseMonitorService;
    @Autowired
    private MockMvc mvc;

    private final List<CourseResponse> coursesResponseList = new ArrayList<>(
            List.of(new CourseResponse(1L, "JavaEE", new Date(), true)));

    @Test
    void getCoursesStartedOnDay() throws Exception {
        Mockito.when(courseMonitorService.getCoursesStartedOnDay("2025-05-31"))
                .thenReturn(Optional.of(coursesResponseList));
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/monitor-courses/courses/startedOnDay=2025-05-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(coursesResponseList.get(0).getId()))
                .andExpect(jsonPath("$.[0].name").value(coursesResponseList.get(0).getName()))
                .andExpect(jsonPath("$.[0].isActive").value(coursesResponseList.get(0).isActive()));
    }

    @Test
    void getCoursesStartedToday() throws Exception {
        Mockito.when(courseMonitorService.getCoursesStartedToday())
                .thenReturn(Optional.of(coursesResponseList));
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/monitor-courses/courses/startedToday"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(coursesResponseList.get(0).getId()))
                .andExpect(jsonPath("$.[0].name").value(coursesResponseList.get(0).getName()))
                .andExpect(jsonPath("$.[0].isActive").value(coursesResponseList.get(0).isActive()));
    }
}
