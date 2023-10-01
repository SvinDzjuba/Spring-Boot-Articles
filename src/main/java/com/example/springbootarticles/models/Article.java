package com.example.springbootarticles.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
    @Field("user_id")
    private String author_id;
    private int favoriteCount;
    private String[] tagList;

    public Article setArticle(Article article, User author) {
        return new Article(
                this.id,
                article.getTitle(),
                article.getDemo(),
                article.getContent(),
                this.created_at,
                new Date(),
                author.getId(),
                article.getFavoriteCount(),
                article.getTagList()
        );
    }
}
