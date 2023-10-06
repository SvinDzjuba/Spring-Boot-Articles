package com.example.springbootarticles.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Author {
    private String name;
    private String username;
    private String bio;
}
