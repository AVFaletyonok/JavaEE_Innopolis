package org.example.client;

import org.example.dto.student.StudentResponse;

import java.util.List;

public interface StudentsClient {

    List<StudentResponse> getStudentsByCourse(final String courseName);

}
