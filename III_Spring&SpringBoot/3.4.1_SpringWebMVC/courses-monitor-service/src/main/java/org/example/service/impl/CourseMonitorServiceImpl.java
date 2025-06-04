package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.client.CoursesClient;
import org.example.client.StudentsClient;
import org.example.dto.course.CourseResponse;
import org.example.dto.student.StudentResponse;
import org.example.mail.MailSender;
import org.example.service.CourseMonitorService;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseMonitorServiceImpl implements CourseMonitorService {

    private final CoursesClient coursesClient;
    private final StudentsClient studentsClient;
    private final MailSender mailSender;

    @Override
    public void checkCourses() {
        Optional<List<CourseResponse>> startedCoursesOpt = getCoursesStartedToday();
        if (startedCoursesOpt.isEmpty()) {
            return;
        }

        List<CourseResponse> startedCourses = startedCoursesOpt.get();
        List<StudentResponse> students = null;
        for (CourseResponse course : startedCourses) {
            students = studentsClient.getStudentsByCourse(course.getName());

            for(StudentResponse student : students) {
                mailSender.sendNotificationMessage(student, course.getName());
            }
        }
    }

    @Override
    public Optional<List<CourseResponse>> getCoursesStartedOnDay(final String day) {
        // YYYY-MM-DD
        try {
            LocalDate checkingDate = LocalDate.parse(day);
            List<CourseResponse> courses = coursesClient.getCourses();

            List<CourseResponse> resultCourses = new ArrayList<>();
            for(CourseResponse course : courses) {
                LocalDate courseStartDate = course.getStartDate()
                                                    .toInstant()
                                                    .atZone(ZoneId.systemDefault())
                                                    .toLocalDate();
                if (courseStartDate.equals(checkingDate)) {
                    resultCourses.add(course);
                }
            }
            return resultCourses.isEmpty()
                    ? Optional.empty()
                    : Optional.of(resultCourses);
        } catch (DateTimeException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<CourseResponse>> getCoursesStartedToday() {
        String currentDay = LocalDate.now().toString();
        return getCoursesStartedOnDay(currentDay);
    }
}