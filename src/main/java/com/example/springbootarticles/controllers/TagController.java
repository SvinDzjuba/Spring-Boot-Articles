package com.example.springbootarticles.controllers;

import com.example.springbootarticles.services.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@Tag(name = "Tags", description = "Tags API")
public class TagController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/tags")
    @Operation(summary = "Get Tags")
    @SecurityRequirements
    public String[] getTags() {
        return articleService.getTagsList();
    }
}
