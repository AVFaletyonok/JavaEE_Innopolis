package org.example.service;

import org.example.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    public boolean isStudentCorrect(final Student student);

    Optional<Student> getStudent(final Long studentId);

    Optional<Student> createStudent(final Student student);

    Optional<Student> updateStudent(final Student student);

    Optional<Student> deleteStudent(final Long studentId);

    List<Student> getStudents();

    Optional<Student> signUpCourse(final Long studentId, final String courseName);
}
