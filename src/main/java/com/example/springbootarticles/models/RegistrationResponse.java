package com.example.springbootarticles.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RegistrationResponse {
    private String username;
    private String email;
    private String bio;
    private String token;
}
