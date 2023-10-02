package com.example.springbootarticles.models;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CommentResponse {
    private String id;
    private String content;
    private Date created_at;
    private Date updated_at;
    private User author;
    private Article article;
}
