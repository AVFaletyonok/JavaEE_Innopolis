
//package org.example.service.impl;
//
//import org.example.dto.student.StudentResponse;
//import org.example.model.Student;
//import org.example.service.StudentService;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Primary;
//import org.springframework.context.annotation.Profile;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
///*
//*   default values if bd is fall down
// */
//@Service(value = "mockStudentService")
//@ConditionalOnProperty(prefix = "mock", name = "student.service", havingValue = "true")
////@Primary
////@Profile("mock")
//public class MockStudentServiceImpl implements StudentService {
//    @Override
//    public Optional<StudentResponse> getStudent(Long studentId) {
//        return Optional.of(new StudentResponse(1L, "Ivan", "Ivanov",
//                            null, "ivan@mail.ru", null));
//    }
//
//    @Override
//    public Optional<StudentResponse> createStudent(Student request) {
//        return Optional.of(new StudentResponse(1L, "Ivan", "Ivanov",
//                null, "ivan@mail.ru", null));
//    }
//
//    @Override
//    public List<StudentResponse> getStudents() {
//        return List.of(new StudentResponse(1L, "Ivan", "Ivanov",
//                        null, "ivan@mail.ru", null));
//    }
//}
