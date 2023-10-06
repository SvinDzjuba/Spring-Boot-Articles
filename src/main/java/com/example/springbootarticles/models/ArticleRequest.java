package com.example.springbootarticles.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ArticleRequest {
    private String title;
    private String demo;
    private String content;
    private String[] tagList;
}
