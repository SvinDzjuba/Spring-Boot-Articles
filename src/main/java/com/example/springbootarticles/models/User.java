package com.example.springbootarticles.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document("users")
public class User {
    @Id
    private String id;
    private String name;
    private String username;
    private String role;
    private String email;
    private String password;
    private Date created_at;
    private Date updated_at;
    private Subscription subscription;

    public User(String id, String name, String username, String role, String email, String password, Date created_at, Date updated_at, Subscription subscription) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.role = role;
        this.email = email;
        this.password = password;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.subscription = subscription;
    }

    public Subscription getSubscriptionInfo() {
        return this.subscription;
    }
    
    public String getSubscription() {
        return
                "\n\tPlan: " + subscription.getPlan() +
                "\n\tCounter: " + subscription.getCounter() +
                "\n\tExpiration Date: " + subscription.getExpDate() +
                "\n\tCurrent Month: " + subscription.getCurrentMonth();
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }
}