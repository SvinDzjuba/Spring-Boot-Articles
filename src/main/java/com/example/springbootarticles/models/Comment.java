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
@ToString
@Document("comments")
public class Comment {
    @Id
    private String id;
//    private ObjectId user_id;
//    private ObjectId article_id;
    private String user_id;
    private String article_id;
    private String content;
    private Date created_at;
    private Date updated_at;
}

