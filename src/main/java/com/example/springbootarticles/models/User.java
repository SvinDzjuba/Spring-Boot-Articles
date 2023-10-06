package com.example.springbootarticles.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("users")
public class User {
    @Id
    private String id;
    private String name;
    @Indexed(unique = true)
    private String username;
    private String bio;
    @Indexed(unique = true)
    private String email;
    private String password;
    private Date created_at;
    private Date updated_at;
    private String[] followers;
    private String[] following;
    private String[] favoriteArticles;
    @Getter @Setter
    private Subscription subscription;
}