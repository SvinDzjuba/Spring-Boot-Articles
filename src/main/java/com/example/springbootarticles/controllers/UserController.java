package com.example.springbootarticles.controllers;

import com.example.springbootarticles.exceptions.NotFoundException;
import com.example.springbootarticles.models.User;
import com.example.springbootarticles.models.UserRequest;
import com.example.springbootarticles.repositories.UserRepository;
import com.example.springbootarticles.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;

@RestController
@RequestMapping("api")
@Tag(name = "Users", description = "Users API")
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    @Operation(summary = "Get all users")
    public List<User> getUsers() {
        return userRepo.findAll();
    }

    @PutMapping("/user")
    @Operation(summary = "Update User")
    public ResponseEntity<?> updateUser(@RequestBody UserRequest user) {
        try {
            userService.updateUserHandler(user);
            return new ResponseEntity<>("User was updated successfully!", HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/user")
    @Operation(summary = "Delete Current User")
    public ResponseEntity<?> deleteUser() {
        try {
            userService.deleteUserHandler();
            return new ResponseEntity<>("User was deleted successfully!", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user")
    @Operation(summary = "Get Current User")
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepo.findByUsername(authentication.getName());
    }

    @PutMapping("/user/subscription")
    @Operation(summary = "Upgrade User Subscription")
    public String upgradeSubscription(@RequestParam @Parameter(name = "subscription", description = "Silver Month") String subscription) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findByUsername(authentication.getName());
        userService.upgradeUserSubscription(subscription, user);
        return "User plan upgraded to " + subscription;
    }

    @GetMapping("/user/followers")
    @Operation(summary = "Get Followers")
    public ResponseEntity<?> getFollowers() {
        try {
            return new ResponseEntity<>(userService.showFollowersOrFollowing("followers"), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/following")
    @Operation(summary = "Get Following")
    public ResponseEntity<?> getFollowing() {
        try {
            return new ResponseEntity<>(userService.showFollowersOrFollowing("following"), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
