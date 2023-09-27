package com.example.springbootarticles.controllers;

import com.example.springbootarticles.models.Article;
import com.example.springbootarticles.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepo;

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
    public Article showArticle(@PathVariable String id) {
        // Get authorized user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();

        } else {
            // Demo here
        }
    }
}
