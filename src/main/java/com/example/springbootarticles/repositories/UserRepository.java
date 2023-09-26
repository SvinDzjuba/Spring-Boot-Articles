package com.example.springbootarticles.repositories;

import com.example.springbootarticles.models.Article;
import com.example.springbootarticles.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    User findUserById(String id);
    @Query(value="{email:'?0'}")
    User findUserByEmail(String email);
    @Query(value="{username:'?0', password:'?1'}")
    User findUserByUsernameAndPassword(String username, String password);
    User findByUsername(String username);
}
