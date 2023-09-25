package com.example.springbootarticles.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("comments")
public class Comment {
    @Id
    private String id;
    private ObjectId user_id;
    private ObjectId article_id;
    private String content;
    private Date created_at;
    private Date updated_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ObjectId getUser_id() {
        return user_id;
    }

    public void setUser_id(ObjectId user_id) {
        this.user_id = user_id;
    }

    public ObjectId getArticle_id() {
        return article_id;
    }

    public void setArticle_id(ObjectId article_id) {
        this.article_id = article_id;
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

    public Comment(String id, ObjectId user_id, ObjectId article_id, String content, Date created_at, Date updated_at) {
        this.id = id;
        this.user_id = user_id;
        this.article_id = article_id;
        this.content = content;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
