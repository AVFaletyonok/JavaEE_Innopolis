package org.example.controller;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @GetMapping("/home")
    public String getHome(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        return "home";
    }
}
