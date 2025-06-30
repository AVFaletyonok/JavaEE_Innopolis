package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.student.StudentResponse;
import org.example.entity.StudentEntity;
import org.example.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/students")
public class RegistrationController {

    private final StudentService studentService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new StudentEntity());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("userForm") @Valid StudentEntity newStudent,
                          BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "registration";
        }
        if (!newStudent.getPassword().equals(newStudent.getPasswordConfirm())){
            model.addAttribute("passwordError", "Пароли не совпадают");
            return "registration";
        }
        Optional<StudentResponse> student = studentService.createStudent(newStudent);
        if (student.isEmpty()){
            model.addAttribute("firstNameError", "Студент с таким именем уже существует");
            return "registration";
        }

        // Добавляем флаг успешной регистрации и ID студента в модель
        model.addAttribute("registrationSuccess", true);
        model.addAttribute("studentId", student.get().getId());
        return "registration";
    }
}
