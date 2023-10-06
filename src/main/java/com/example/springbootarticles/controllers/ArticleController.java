package com.example.springbootarticles.controllers;

import com.example.springbootarticles.exceptions.NotFoundException;
import com.example.springbootarticles.models.*;
import com.example.springbootarticles.repositories.ArticleRepository;
import com.example.springbootarticles.services.ArticleService;
import com.example.springbootarticles.services.CustomService;
import com.example.springbootarticles.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/articles")
@Tag(name = "Articles", description = "Articles API")
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CustomService customService;

    /* CRUD for articles */
    @GetMapping("/")
    @Operation(summary = "List Articles")
    @SecurityRequirements
    public List<ArticleResponse> getArticles(@RequestParam(required = false) String tag) {
        List<Article> articles;
        if (tag == null) {
            articles = articleRepo.findAll();
        } else {
            articles = articleRepo.findByTagListContaining(tag);
        }
        List<ArticleResponse> articlesList = new ArrayList<>();
        for (Article article : articles) {
            articlesList.add(articleService.getArticleWithDetails(article.getId(), "DEMO"));
        }
        return articlesList;
    }

    @PostMapping("/")
    @Operation(summary = "Create Article")
    public ResponseEntity<?> saveArticle(@RequestBody ArticleRequest article) {
        try {
            articleService.createArticleHandler(article);
            return new ResponseEntity<>("Article was created successfully!", HttpStatus.CREATED);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Article")
    public ResponseEntity<?> updateArticle(@PathVariable("id") String id, @RequestBody ArticleRequest article) {
        try {
            articleService.updateArticleHandler(id, article);
            return new ResponseEntity<>("Article was updated successfully!", HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Article")
    public ResponseEntity<?> deleteArticle(@PathVariable String id) {
        try {
            articleService.deleteArticleHandler(id);
            return new ResponseEntity<>("Article was deleted successfully!", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Article")
    public ArticleResponse showArticle(@PathVariable String id) throws RuntimeException {
        Optional<Article> article = articleRepo.findById(id);
        if (article.isPresent()) {
            return userService.checkUserSubscriptionAvailability(id);
        } else {
            throw new NotFoundException("Article not found!");
        }
    }

    @PostMapping("/articles/{id}/favorite")
    @Operation(summary = "Favorite Article")
    public ResponseEntity<?> addArticleToFavorite(@PathVariable String id) {
        try {
            articleService.likeArticle(id, true);
            String message = "Article was add to favorites successfully!";
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/articles/{id}/favorite")
    @Operation(summary = "Unfavorite Article")
    public ResponseEntity<?> removeArticleFromFavorites(@PathVariable String id) {
        try {
            articleService.likeArticle(id, false);
            String message = "Article was removed from favorites successfully!";
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
