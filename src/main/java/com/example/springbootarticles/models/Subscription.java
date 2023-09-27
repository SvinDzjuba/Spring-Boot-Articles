package com.example.springbootarticles.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Subscription {
    private int remains;
    private Date expires_at;
    private String plan;

    public Subscription(int remains, Date expires_at, String plan) {
        this.remains = remains;
        this.expires_at = expires_at;
        this.plan = plan;
    }
}
