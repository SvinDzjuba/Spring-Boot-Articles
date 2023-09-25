package com.example.springbootarticles.controllers;

import com.example.springbootarticles.models.User;
import com.example.springbootarticles.repositories.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController @RequestMapping("api")
public class UserController {
    UserRepository userRepository;
    @GetMapping("users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("current-user")
    public String getLoggedInUser(Principal principal) {
        return principal.getName();
    }
}