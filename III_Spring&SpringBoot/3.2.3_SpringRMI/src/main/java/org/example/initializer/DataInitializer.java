package org.example.initializer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.entity.Student;
import org.example.repository.StudentRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final StudentRepository studentRepository;

    @PostConstruct
    public void init() {
        if (studentRepository.count() == 0) {
            studentRepository.saveAndFlush(new Student(null, "Ivan", "Vasil'ev",
                                                null, "i_vasilev@mail.ru", new ArrayList<>()));
            studentRepository.saveAndFlush(new Student(null, "Peter", "TheFirst",
                                                null, "p_first@mail.ru", new ArrayList<>()));
            studentRepository.saveAndFlush(new Student(null, "Oleg", "Ivanov",
                                                null, "o_ivanov@mail.ru", new ArrayList<>()));
            studentRepository.saveAndFlush(new Student(null, "Sergey", "Sergeev",
                                                null, "s_sergeev@mail.ru", new ArrayList<>()));
            studentRepository.saveAndFlush(new Student(null, "Uriy", "Gagarin",
                                                null, "u_gagarin@mail.ru", new ArrayList<>()));
        }
    }
}
