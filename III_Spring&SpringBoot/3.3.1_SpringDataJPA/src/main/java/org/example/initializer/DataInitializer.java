package org.example.initializer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.entity.StudentEntity;
import org.example.entity.StudentGroupEntity;
import org.example.repository.StudentGroupRepository;
import org.example.repository.StudentRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final StudentRepository studentRepository;
    private final StudentGroupRepository studentGroupRepository;

    @PostConstruct
    public void init() {
        if (studentRepository.count() == 0) {
            StudentGroupEntity studentGroup = new StudentGroupEntity();
            studentGroup.setName("Java_programmers");
            List<StudentEntity> students = new ArrayList<>();
            StudentEntity student1 = new StudentEntity(null, "Ivan", "Vasil'ev", null,
                                                        LocalDate.of(1980, 2, 21),
                                                        "i_vasilev@mail.ru", studentGroup, new ArrayList<>(List.of(4L)));
            StudentEntity student2 = new StudentEntity(null, "Peter", "TheFirst", null,
                                                        LocalDate.of(1990, 3, 3),
                                                        "p_first@mail.ru", studentGroup, new ArrayList<>());
            StudentEntity student3 = new StudentEntity(null, "Oleg", "Ivanov", null,
                                                        LocalDate.of(1999, 9, 9),
                                                        "o_ivanov@mail.ru", studentGroup, new ArrayList<>(List.of(4L)));
            StudentEntity student4 = new StudentEntity(null, "Sergey", "Sergeev", null,
                                                        LocalDate.of(2000, 10, 10),
                                                        "s_sergeev@mail.ru", studentGroup, new ArrayList<>());
            StudentEntity student5 = new StudentEntity(null, "Uriy", "Gagarin", null,
                                                        LocalDate.of(2011, 11, 11),
                                                        "u_gagarin@mail.ru", studentGroup, new ArrayList<>());
            students.add(student1);
            students.add(student2);
            students.add(student3);
            students.add(student4);
            students.add(student5);
            studentGroup.setStudents(students);
            studentGroupRepository.saveAndFlush(studentGroup);
            studentRepository.saveAndFlush(student1);
            studentRepository.saveAndFlush(student2);
            studentRepository.saveAndFlush(student3);
            studentRepository.saveAndFlush(student4);
            studentRepository.saveAndFlush(student5);
        }
    }
}
