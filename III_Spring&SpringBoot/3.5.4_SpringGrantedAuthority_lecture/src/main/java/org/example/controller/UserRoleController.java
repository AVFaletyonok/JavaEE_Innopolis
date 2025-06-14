package org.example.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRoleController {

    @GetMapping("/user-role")
    public String getUserRole(Authentication authentication) {
        if (authentication == null) {
            return "USER";
        }
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        return isAdmin ? "ADMIN" : "USER";
    }
}
