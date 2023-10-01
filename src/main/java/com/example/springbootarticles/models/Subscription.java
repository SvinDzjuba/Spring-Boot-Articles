package com.example.springbootarticles.models;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Subscription {
    private int remains;
    private Date expires_at;
    private Date resets_at;
    private String plan;
}
