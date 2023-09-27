package com.example.springbootarticles.models;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.Date;
import java.util.List;

@Getter
@Setter
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

    public Article(String title, String demo, String content, Date created_at, Date updated_at, ObjectId user_id, Tag[] tagList, int favoriteCount) {
        super();
        this.title = title;
        this.demo = demo;
        this.content = content;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.user_id = user_id;
        this.tagList = tagList;
        this.favoriteCount = favoriteCount;
    }
}
