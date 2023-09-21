package com.example.springbootarticles.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("users")
public class User {
    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private Date created_at;
    private Date updated_at;
    private Subscription subscription;

    public User(String id, String name, String email, String password, Date created_at, Date updated_at, Subscription subscription) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.subscription = subscription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
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