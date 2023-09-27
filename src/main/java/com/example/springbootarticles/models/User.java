package com.example.springbootarticles.models;

import lombok.*;
import org.springframework.data.annotation.Id;
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
    private String username;
    private String role;
    private String email;
    private String password;
    private Date created_at;
    private Date updated_at;
    @Getter @Setter
    private Subscription subscription;
}