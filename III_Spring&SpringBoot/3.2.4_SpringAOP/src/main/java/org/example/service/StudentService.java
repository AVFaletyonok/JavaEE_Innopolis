package org.example.service;

import org.example.dto.student.StudentResponse;
import org.example.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    Optional<StudentResponse> getStudent(final Long studentId);

    Optional<StudentResponse> createStudent(final Student student);

    Optional<StudentResponse> updateStudent(final Student student);

    Optional<StudentResponse> deleteStudent(final Long studentId);

    List<StudentResponse> getStudents();

    Optional<StudentResponse> signUpCourse(final Long studentId, final String courseName);

    Optional<List<StudentResponse>> getStudentsByCourse(final String courseName);
}
