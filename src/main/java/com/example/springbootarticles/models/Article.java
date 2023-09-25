package com.example.springbootarticles.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.Date;
import java.util.List;


@Document("articles")
public class Article {
    @Id
    private String id;
    private String title;
    private String content;
    private Date created_at;
    private Date updated_at;
    private ObjectId user_id;
    private int favoriteCount;
    private Tag[] tagList;

    public Article(String title, String content, Date created_at, Date updated_at, ObjectId user_id, Tag[] tagList, int favoriteCount) {
        super();
        this.title = title;
        this.content = content;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.user_id = user_id;
        this.tagList = tagList;
        this.favoriteCount = favoriteCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public ObjectId getUser_id() {
        return user_id;
    }

    public void setUser_id(ObjectId user_id) {
        this.user_id = user_id;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public Tag[] getTagList() {
        return tagList;
    }

    public void setTagList(Tag[] tagList) {
        this.tagList = tagList;
    }
}
