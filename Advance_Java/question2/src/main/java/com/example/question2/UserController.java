package com.example.question2;

import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String name,
            @RequestParam String email) {

        return "User Registered: " + name + " | Email: " + email;
    }
}