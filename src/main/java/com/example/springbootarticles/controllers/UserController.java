package com.example.springbootarticles.controllers;

import com.example.springbootarticles.exceptions.NotFoundException;
import com.example.springbootarticles.models.User;
import com.example.springbootarticles.repositories.UserRepository;
import com.example.springbootarticles.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserService userService;

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
    public ResponseEntity<?> updateUser(@PathVariable("id") String id, @RequestBody User user) {
        try {
            userService.updateUserHandler(id, user);
            return new ResponseEntity<>("User was updated successfully!", HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable String id) {
        userRepo.deleteById(id);
        return "User with id:{" + id + "} was deleted successfully";
    }

    @PutMapping("/users/{id}/subscription")
    public String upgradeSubscription(@RequestParam String subscription, @PathVariable String id) {
        User user = userRepo.findById(id).orElseThrow(() -> new NotFoundException("User not found!"));
        userService.upgradeUserSubscription(subscription, user);
        return "User plan upgraded to " + subscription;
    }

}
