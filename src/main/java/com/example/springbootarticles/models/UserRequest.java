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
public class UserRequest {
    @NotBlank
    @Schema(name = "name", description = "Name of user", example = "John Doe")
    private String name;
    @NotBlank
    @Schema(name = "email", description = "Email of user", example = "john.doe@test.ee")
    private String email;
    @NotBlank
    @Schema(name = "username", description = "Username of user", example = "johndoe")
    private String username;
    @Schema(name = "bio", description = "Bio of user", example = "I am a software engineer")
    private String bio;
    @NotBlank
    @Schema(name = "password", description = "Password of user", example = "123456")
    private String password;
}
