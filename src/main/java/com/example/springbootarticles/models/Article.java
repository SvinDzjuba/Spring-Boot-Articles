package com.example.springbootarticles.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Document("articles")
public class Article {
    @Id
    private String id;
    @NotBlank(message = "Title is mandatory")
    private String title;
    @NotBlank(message = "Slug is mandatory")
    private String slug;
    @NotBlank(message = "Demo is mandatory")
    private String demo;
    @NotBlank(message = "Content is mandatory")
    private String content;
    private Date created_at;
    private Date updated_at;
    private String author;
    private int favoriteCount;
    private String[] tagList;
}
