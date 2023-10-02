package com.example.springbootarticles.repositories;

import com.example.springbootarticles.models.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    // Queries for comments
}
