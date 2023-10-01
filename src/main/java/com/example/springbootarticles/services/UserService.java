package com.example.springbootarticles.services;

import com.example.springbootarticles.exceptions.NotFoundException;
import com.example.springbootarticles.models.User;
import com.example.springbootarticles.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void updateUserHandler(String id, User user) throws NotFoundException {
        Optional<User> userData = userRepo.findById(id);
        if (userData.isPresent()){
            User userToUpdate = userData.get();
            userToUpdate.setName(user.getName() != null ? user.getName() : userToUpdate.getName());
            userToUpdate.setUsername(user.getUsername() != null ? user.getUsername() : userToUpdate.getUsername());
            userToUpdate.setRole(user.getRole() != null ? user.getRole() : userToUpdate.getRole());
            userToUpdate.setEmail(user.getEmail() != null ? user.getEmail() : userToUpdate.getEmail());
            userToUpdate.setPassword(user.getPassword() != null ? passwordEncoder.encode(user.getPassword()) : userToUpdate.getPassword());
            userToUpdate.setUpdated_at(new Date());
            userToUpdate.setSubscription(user.getSubscription() != null ? user.getSubscription() : userToUpdate.getSubscription());
            userRepo.save(userToUpdate);
        } else {
            throw new NotFoundException("User not found!");
        }
    }
}
