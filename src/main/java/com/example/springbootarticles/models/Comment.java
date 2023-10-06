package com.example.springbootarticles.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
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
    @NotBlank
    private String user_id;
    @NotBlank
    private String article_id;
    @NotBlank
    private String content;
    private Date created_at;
    private Date updated_at;
}

