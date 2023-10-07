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
public class JwtRequest {
    @NotBlank
    @Schema(name = "username", description = "Username", example = "durgesh")
    private String username;
    @NotBlank
    @Schema(name = "password", description = "Password", example = "123456")
    private String password;
}
