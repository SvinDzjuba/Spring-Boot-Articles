package com.example.springbootarticles.models;

public class Tag {
    private String name;

    public Tag() {
        // Default constructor
    }

    public Tag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
