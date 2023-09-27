package com.example.springbootarticles.models;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("articles")
public class Article {
    @Id
    private String id;
    private String title;
    private String demo;
    private String content;
    private Date created_at;
    private Date updated_at;
    private ObjectId user_id;
    private int favoriteCount;
    private Tag[] tagList;

    public Article demoArticle(Article article) throws InstantiationException, IllegalAccessException {
        Article demoArticle = Article.class.newInstance();
        demoArticle.setTitle(article.getTitle());
        demoArticle.setDemo(article.getDemo());
        demoArticle.setFavoriteCount(article.getFavoriteCount());
        demoArticle.setTagList(article.getTagList());
        demoArticle.setCreated_at(article.getCreated_at());
        demoArticle.setUpdated_at(article.getUpdated_at());
        demoArticle.setUser_id(article.getUser_id());
        return demoArticle;
    }
}
