package com.example.springbootarticles.controllers;

import com.example.springbootarticles.exceptions.NotFoundException;
import com.example.springbootarticles.models.Article;
import com.example.springbootarticles.models.ArticleResponse;
import com.example.springbootarticles.models.User;
import com.example.springbootarticles.repositories.ArticleRepository;
import com.example.springbootarticles.repositories.UserRepository;
import com.example.springbootarticles.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ArticleService articleService;

    /* CRUD for articles */
    @GetMapping("/articles")
    public List<ArticleResponse> getArticles(){
        List<Article> articles = articleRepo.findAll();
        List<ArticleResponse> articlesList = new ArrayList<>();
        for (Article article : articles) {
            articlesList.add(articleService.getArticleWithDetails(article.getId(), "DEMO"));
        }
        return articlesList;
    }

    @PostMapping("/articles")
    public String saveArticle(@RequestBody Article article){
        articleRepo.save(article);
        return "Added article with title: " + article.getTitle();
    }

    @PutMapping("/articles/{id}")
    public ResponseEntity<?> updateArticle(@PathVariable("id") String id, @RequestBody Article article) {
        try {
            articleService.updateArticleHandler(id, article);
            return new ResponseEntity<>("Article was updated successfully!", HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/articles/{id}")
    public String deleteArticle(@PathVariable String id){
        articleRepo.deleteById(id);
        return "Article with id: {" + id + "} was deleted successfully!";
    }

    /* Read particular article */
    @GetMapping("/articles/{id}")
    public ArticleResponse showArticle(@PathVariable String id) throws ParseException, RuntimeException {
        Optional<Article> article = articleRepo.findById(id);
        // Get authorized user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (article.isPresent()) {
            if (authentication != null && authentication.isAuthenticated()) {
                String username = authentication.getName();
                User user = userRepo.findByUsername(username);
                // Convert expiration and current date to string then compare them in Date format
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String currentDate = formatter.format(new Date());
                String expirationDate = formatter.format(user.getSubscription().getExpires_at());
                if (formatter.parse(expirationDate).after(formatter.parse(currentDate))) {
                    // Subscription is active
                    if (user.getSubscription().getRemains() > 0) {
                        user.getSubscription().setRemains(user.getSubscription().getRemains() - 1);
                        userRepo.save(user);
                        return articleService.getArticleWithDetails(id, "ORIGINAL");
                    }
                    return articleService.getArticleWithDetails(id, "DEMO");
                }
            }
            // Non authenticated user - demo article
            return articleService.getArticleWithDetails(id, "DEMO");
        } else {
            throw new NotFoundException("Article not found!");
        }
    }
}
