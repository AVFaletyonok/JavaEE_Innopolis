package org.example.initializer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.entity.StudentEntity;
import org.example.repository.StudentRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final StudentRepository studentRepository;

    @PostConstruct
    public void init() {
        if (studentRepository.count() == 0) {
            StudentEntity student1 = new StudentEntity(null, "Alexander", "Faletyonok", "Victorovich",
                                                        LocalDate.of(1980, 2, 21),
                                                        "AVFaletyonok@yandex.ru", new ArrayList<>(), new ArrayList<>());
            StudentEntity student2 = new StudentEntity(null, "Peter", "TheFirst", null,
                                                        LocalDate.of(1990, 3, 3),
                                                        "p_first@mail.ru", new ArrayList<>(), new ArrayList<>());
            StudentEntity student3 = new StudentEntity(null, "Oleg", "Ivanov", null,
                                                        LocalDate.of(1999, 9, 9),
                                                        "o_ivanov@mail.ru", new ArrayList<>(), new ArrayList<>());
            StudentEntity student4 = new StudentEntity(null, "Sergey", "Sergeev", null,
                                                        LocalDate.of(2000, 10, 10),
                                                        "s_sergeev@mail.ru", new ArrayList<>(), new ArrayList<>());
            StudentEntity student5 = new StudentEntity(null, "Uriy", "Gagarin", null,
                                                        LocalDate.of(2011, 11, 11),
                                                        "u_gagarin@mail.ru", new ArrayList<>(), new ArrayList<>());
            studentRepository.saveAndFlush(student1);
            studentRepository.saveAndFlush(student2);
            studentRepository.saveAndFlush(student3);
            studentRepository.saveAndFlush(student4);
            studentRepository.saveAndFlush(student5);
        }
    }
}
