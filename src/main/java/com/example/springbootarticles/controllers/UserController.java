package com.example.springbootarticles.controllers;

import com.example.springbootarticles.models.User;
import com.example.springbootarticles.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepo.findAll();
    }

    @PostMapping("/users")
    public String saveUser(@RequestBody User user) {
        userRepo.save(user);
        return "Added user with email: " + user.getEmail();
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") String id, @RequestBody User user) {
        User userData = userRepo.findById(id).orElse(null);
        if (userData != null) {
            User newUser = new User(
                    id,
                    user.getName(),
                    user.getUsername(),
                    user.getRole(),
                    user.getEmail(),
                    user.getPassword(),
                    userData.getCreated_at(),
                    userData.getUpdated_at(),
                    userData.getSubscription()
            );
            return new ResponseEntity<>(userRepo.save(newUser), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable String id) {
        userRepo.deleteById(id);
        return "User with id:{" + id + "} was deleted successfully";
    }
}
