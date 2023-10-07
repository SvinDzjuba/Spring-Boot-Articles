package com.example.springbootarticles.repositories;

import com.example.springbootarticles.models.Article;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ArticleRepository extends MongoRepository<Article, String> {
    // Queries for articles
    List<Article> findByTagListContaining(String tag);
    List<Article> findByAuthor(String author);
    Article findBySlug(String slug);
}