package org.example.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user-info")
public class UserInfoController {

    @GetMapping
    public Map<String, String> getUserInfo(Authentication authentication) {
        Map<String, String> userInfo = new HashMap<>();
        if (authentication != null && authentication.getPrincipal() instanceof DefaultOidcUser) {
            DefaultOidcUser user = (DefaultOidcUser) authentication.getPrincipal();
            userInfo.put("email", user.getEmail());
        }
        return userInfo;
    }
}
