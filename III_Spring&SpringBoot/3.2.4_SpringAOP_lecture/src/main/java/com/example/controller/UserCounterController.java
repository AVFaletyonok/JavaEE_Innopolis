package com.example.controller;

import com.example.counter.OnlineUserCounter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user_counter")
public class UserCounterController {
    private final OnlineUserCounter counter;

    public UserCounterController(OnlineUserCounter counter) {
        this.counter = counter;
    }

    @GetMapping
    public String getUserCount() {
        return "<h1>The site was visited by " + counter.getOnlineUsers() +
                " visitors.</h1>";
    }
}
