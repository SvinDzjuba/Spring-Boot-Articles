package com.example.springbootarticles.repositories;

import com.example.springbootarticles.models.Article;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ArticleRepository extends MongoRepository<Article, String> {
    // Queries for articles
}