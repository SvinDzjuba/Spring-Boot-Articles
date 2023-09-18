package com.example.springbootarticles.repositories;

import com.example.springbootarticles.models.Article;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ArticleRepository extends MongoRepository<Article, String> {

    @Query(value="{id:'?0'}")
    Article findArticleById(String id);

    @Query(value="{'tagList': { $elemMatch: { 'name': '?0' } }}", fields="{'title' : 1, 'content' : 1, 'tagList.name': 1}")
    List<Article> findAll(String tag);
}