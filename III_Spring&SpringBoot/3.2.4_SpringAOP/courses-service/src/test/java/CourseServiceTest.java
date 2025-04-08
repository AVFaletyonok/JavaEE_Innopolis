import org.example.dto.CoursesResponse;
import org.example.entity.Course;
import org.example.repository.CourseRepository;
import org.example.service.CourseService;
import org.example.service.impl.CourseServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ContextConfiguration(classes = CourseServiceImpl.class)
public class CourseServiceTest {

    @Autowired
    private CourseService courseService;
    @MockitoBean
    private CourseRepository courseRepository;

    private Course correctCourse = new Course(1L, "JavaEE", new Date(), true, false);
    private Course wrongCourse = new Course(10L, null, new Date(), true, false);

    @Test
    public void getCourseExisting() {
        Mockito.when(courseRepository.findById(correctCourse.getId()))
                .thenReturn(Optional.of(correctCourse));

        Optional<CoursesResponse> actualCoursesResponse = courseService.getCourse(correctCourse.getId());
        assertTrue(actualCoursesResponse.isPresent());
        CoursesResponse actualCourseResponse = actualCoursesResponse.get();
        assertEquals(correctCourse.getId(), actualCourseResponse.getId());
        assertEquals(correctCourse.getName(), actualCourseResponse.getName());
        assertEquals(correctCourse.isActive(), actualCourseResponse.isActive());
    }

    @Test
    public void getCourseNotExisting() {
        Mockito.when(courseRepository.findById(wrongCourse.getId()))
                .thenReturn(Optional.empty());

        Optional<CoursesResponse> actualCoursesResponse = courseService.getCourse(wrongCourse.getId());
        assertTrue(actualCoursesResponse.isEmpty());
    }

    @Test
    public void createCourseCorrect() {
        Mockito.when(courseRepository.saveAndFlush(correctCourse))
                .thenReturn(correctCourse);
        Mockito.when(courseRepository.findAll())
                .thenReturn(new ArrayList<>());

        Optional<CoursesResponse> actualCoursesResponse = courseService.createCourse(correctCourse);
        assertTrue(actualCoursesResponse.isPresent());
        CoursesResponse actualCourseResponse = actualCoursesResponse.get();
        assertEquals(correctCourse.getId(), actualCourseResponse.getId());
        assertEquals(correctCourse.getName(), actualCourseResponse.getName());
        assertEquals(correctCourse.isActive(), actualCourseResponse.isActive());
    }

    @Test
    public void createCourseWrong() {
        Optional<CoursesResponse> actualCoursesResponse = courseService.createCourse(wrongCourse);
        assertTrue(actualCoursesResponse.isEmpty());
    }

    @Test
    public void updateCourseCorrect() {
        Mockito.when(courseRepository.findById(correctCourse.getId()))
                .thenReturn(Optional.of(correctCourse));
        Mockito.when(courseRepository.saveAndFlush(correctCourse))
                .thenReturn(correctCourse);

        Optional<CoursesResponse> actualCoursesResponse = courseService.updateCourse(correctCourse);
        assertTrue(actualCoursesResponse.isPresent());
        CoursesResponse actualCourseResponse = actualCoursesResponse.get();
        assertEquals(correctCourse.getId(), actualCourseResponse.getId());
        assertEquals(correctCourse.getName(), actualCourseResponse.getName());
        assertEquals(correctCourse.isActive(), actualCourseResponse.isActive());
    }

    @Test
    public void updateCourseWrong() {
        Mockito.when(courseRepository.findById(wrongCourse.getId()))
                .thenReturn(Optional.empty());
        Optional<CoursesResponse> actualCoursesResponse = courseService.updateCourse(wrongCourse);
        assertTrue(actualCoursesResponse.isEmpty());
    }

    @Test
    public void deleteCourseExisting() {
        Mockito.when(courseRepository.findById(correctCourse.getId()))
                .thenReturn(Optional.of(correctCourse));
        Course archivedCurse = new Course(correctCourse.getId(), correctCourse.getName(),
                                        correctCourse.getStartDate(), false, true);
        Mockito.when(courseRepository.saveAndFlush(archivedCurse))
                .thenReturn(archivedCurse);

        Optional<CoursesResponse> actualCoursesResponse = courseService.deleteCourse(correctCourse.getId());
        assertTrue(actualCoursesResponse.isPresent());
        CoursesResponse actualCourseResponse = actualCoursesResponse.get();
        assertEquals(archivedCurse.getId(), actualCourseResponse.getId());
        assertEquals(archivedCurse.getName(), actualCourseResponse.getName());
        assertEquals(archivedCurse.isActive(), actualCourseResponse.isActive());
    }

    @Test
    public void deleteCourseNotExisting() {
        Mockito.when(courseRepository.findById(wrongCourse.getId()))
                .thenReturn(Optional.empty());
        Optional<CoursesResponse> actualCoursesResponse = courseService.deleteCourse(wrongCourse.getId());
        assertTrue(actualCoursesResponse.isEmpty());
    }

    @Test
    public void getCourses() {
        Mockito.when(courseRepository.findAll())
                .thenReturn(List.of(correctCourse));
        Optional<List<CoursesResponse>> actualOptionalCoursesResponse = courseService.getCourses();
        assertTrue(actualOptionalCoursesResponse.isPresent());
        List<CoursesResponse> actualListCourseResponse = actualOptionalCoursesResponse.get();
        CoursesResponse course = actualListCourseResponse.get(0);
        assertEquals(correctCourse.getId(), course.getId());
        assertEquals(correctCourse.getName(), course.getName());
        assertEquals(correctCourse.isActive(), course.isActive());
    }
}
