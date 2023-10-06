package com.example.springbootarticles.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class HomeController {
    @GetMapping("/")
    public RedirectView welcomePage() {
        // Redirect to the swagger ui
        return new RedirectView("/swagger-ui.html");
    }
}
