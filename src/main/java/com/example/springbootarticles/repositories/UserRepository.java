package com.example.springbootarticles.repositories;

import com.example.springbootarticles.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    User findUserById(String id);
}
