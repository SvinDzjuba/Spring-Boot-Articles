package com.example.springbootarticles.models;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ArticleResponse {
    private String title;
    private String demo;
    private String content;
    private User author;
    private Date created_at;
    private Date updated_at;
    private int favoriteCount;
    private String[] tagList;
}
