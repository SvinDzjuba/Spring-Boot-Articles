package com.example.springbootarticles.models;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class JwtRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
