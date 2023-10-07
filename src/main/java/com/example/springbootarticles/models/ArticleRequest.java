package com.example.springbootarticles.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ArticleRequest {
    @NotBlank(message = "Title is mandatory")
    @Schema(name = "title", description = "Title of article", example = "What is Java Spring Boot")
    private String title;
    @NotBlank(message = "Demo is mandatory")
    @Schema(name = "demo", description = "Demo of article", example = "Java is awesome language")
    private String demo;
    @NotBlank(message = "Content is mandatory")
    @Schema(name = "content", description = "Content of article", example = "Java is awesome language. It is used in many places.")
    private String content;
    @Schema(name = "tagList", description = "Tags of article", example = "[\"Java\", \"Programming\"]")
    private String[] tagList;
}
