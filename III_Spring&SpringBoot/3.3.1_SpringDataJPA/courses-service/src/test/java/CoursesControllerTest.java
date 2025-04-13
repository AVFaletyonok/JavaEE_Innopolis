import org.example.controller.CoursesController;
import org.example.dto.CoursesResponse;
import org.example.entity.Course;
import org.example.service.impl.CourseServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CoursesController.class)
@ContextConfiguration(classes = CoursesController.class)
public class CoursesControllerTest {

    @Autowired
    private CoursesController coursesController;
    @MockitoBean
    private CourseServiceImpl courseService;
    @Autowired
    private MockMvc mvc;

    private final Course correctCourse = new Course(1L, "JavaEE", null, true, false);
    private final String correctCourseJson = "{"
                                            + "\"id\":1,"
                                            + "\"name\":\"JavaEE\","
                                            + "\"startDate\":null,"
                                            + "\"isActive\":true,"
                                            + "\"isArchived\":false"
                                            + "}";
    private final CoursesResponse correctCoursesResponse =
                                            new CoursesResponse(correctCourse.getId(),
                                                                correctCourse.getName(),
                                                                correctCourse.isActive());

    private final Course wrongCourse = new Course(10L, "Scala", null, false, false);
    private final String wrongCourseJson = "{"
                                            + "\"id\":10,"
                                            + "\"name\":\"Scala\","
                                            + "\"startDate\":null,"
                                            + "\"isActive\":false,"
                                            + "\"isArchived\":false"
                                            + "}";
    private final CoursesResponse wrongCoursesResponse =
                                            new CoursesResponse(wrongCourse.getId(),
                                                                wrongCourse.getName(),
                                                                wrongCourse.isActive());
    private final CoursesResponse deletedCorrectCoursesResponse =
                                            new CoursesResponse(correctCourse.getId(),
                                                                correctCourse.getName(),
                                                                false);

    @Test
    void createCorrectCourse() throws Exception {
        Mockito.when(courseService.createCourse(correctCourse))
                .thenReturn(Optional.of(correctCoursesResponse));

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(correctCourseJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("JavaEE"))
                .andExpect(jsonPath("$.isActive").value(true));
    }

    @Test
    void createWrongCourse() throws Exception {
        Mockito.when(courseService.createCourse(wrongCourse))
                .thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(wrongCourseJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCorrectCourse() throws Exception {
        Mockito.when(courseService.getCourse(correctCourse.getId()))
                .thenReturn(Optional.of(correctCoursesResponse));

        System.out.println(new Date());

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("JavaEE"))
                .andExpect(jsonPath("$.isActive").value(true));
    }

    @Test
    void getWrongCourse() throws Exception {
        Mockito.when(courseService.getCourse(wrongCourse.getId()))
                .thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/courses/10"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCorrectCourse() throws Exception {
        Mockito.when(courseService.updateCourse(correctCourse))
                .thenReturn(Optional.of(correctCoursesResponse));

        mvc.perform(MockMvcRequestBuilders.put("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(correctCourseJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("JavaEE"))
                .andExpect(jsonPath("$.isActive").value(true));
    }

    @Test
    void updateWrongCourse() throws Exception {
        Mockito.when(courseService.updateCourse(wrongCourse))
                .thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.put("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(wrongCourseJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCorrectCourse() throws Exception {
        Mockito.when(courseService.deleteCourse(correctCourse.getId()))
                .thenReturn(Optional.of(deletedCorrectCoursesResponse));

        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/courses/" + correctCourse.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("JavaEE"))
                .andExpect(jsonPath("$.isActive").value(false));
    }

    @Test
    void deleteWrongCourse() throws Exception {
        Mockito.when(courseService.deleteCourse(wrongCourse.getId()))
                .thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/courses/" + wrongCourse.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCourses() throws Exception {
        Mockito.when(courseService.getCourses())
                .thenReturn(Optional.of(List.of(correctCoursesResponse)));

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].name").value("JavaEE"))
                .andExpect(jsonPath("$.[0].isActive").value(true));
    }
}
