package com.example.springbootarticles.models;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expires_at;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date resets_at;
    private String plan;
}
