package com.example.springbootarticles.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class HomeController {
    @GetMapping("/")
    public RedirectView welcomePage(UriComponentsBuilder uriComponentsBuilder) {
        // Redirect to the swagger ui
        // As application deployed on Tomcat, the context path is added to the url
        // So, we need to get the context path and append it to the url
        String contextPath = uriComponentsBuilder.build().getPath();
        return new RedirectView(contextPath + "/swagger-ui.html");
    }
}
