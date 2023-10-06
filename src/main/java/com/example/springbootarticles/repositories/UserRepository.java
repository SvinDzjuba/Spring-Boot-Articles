package com.example.springbootarticles.repositories;

import com.example.springbootarticles.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
    User findByEmail(String email);
    List<User> findByFavoriteArticlesContaining(String id);
}
