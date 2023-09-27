package com.example.springbootarticles.controllers;

import com.example.springbootarticles.models.Article;
import com.example.springbootarticles.models.User;
import com.example.springbootarticles.repositories.ArticleRepository;
import com.example.springbootarticles.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepo;

    @Autowired
    private UserRepository userRepo;

    /* CRUD for articles */
    @GetMapping("/articles")
    public List<Article> getArticles(){
        return articleRepo.findAll();
    }

    @PostMapping("/articles")
    public String saveArticle(@RequestBody Article article){
        articleRepo.save(article);
        return "Added article with title: " + article.getTitle();
    }

    @PutMapping("/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable("id") String id, @RequestBody Article article)
    {
        Optional<Article> articleData = articleRepo.findById(id);

        if (articleData.isPresent()){
            Article _article = articleData.get();
            _article.setTitle(article.getTitle());
            _article.setDemo(article.getDemo());
            _article.setContent(article.getContent());
            _article.setUser_id(article.getUser_id());
            _article.setCreated_at(article.getCreated_at());
            _article.setUpdated_at(article.getUpdated_at());
            _article.setFavoriteCount(article.getFavoriteCount());
            _article.setTagList(article.getTagList());

            return new ResponseEntity<>(articleRepo.save(_article), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/articles/{id}")
    public String deleteArticle(@PathVariable String id){
        articleRepo.deleteById(id);
        return "Article with id: {" + id + "} was deleted successfully";
    }

    /* Additional requests */
    @GetMapping("/articles/{id}")
    public Article showArticle(@PathVariable String id) throws InstantiationException, IllegalAccessException, ParseException {
        Article article = articleRepo.findById(id).orElse(null);
        // Get authorized user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (article != null) {
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
                        return article;
                    }
                    return article.demoArticle(article);
                }
            }
            // Non authenticated user - demo article
            return article.demoArticle(article);
        } else {
            return null;
        }
    }
}
