package com.example.springbootarticles.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FollowersFollowing {
    private int amount;
    private String[] name;
}
