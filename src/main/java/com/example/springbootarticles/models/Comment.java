package com.example.springbootarticles.models;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document("comments")
public class Comment {
    @Id
    private String id;
    private ObjectId user_id;
    private ObjectId article_id;
    private String content;
    private Date created_at;
    private Date updated_at;

    public Comment(String id, ObjectId user_id, ObjectId article_id, String content, Date created_at, Date updated_at) {
        this.id = id;
        this.user_id = user_id;
        this.article_id = article_id;
        this.content = content;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
