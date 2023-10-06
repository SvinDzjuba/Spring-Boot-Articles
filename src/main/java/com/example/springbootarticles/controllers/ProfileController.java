package com.example.springbootarticles.controllers;

import com.example.springbootarticles.exceptions.NotFoundException;
import com.example.springbootarticles.models.Profile;
import com.example.springbootarticles.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/profiles")
@Tag(name = "Profiles", description = "Profiles API")
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/{username}")
    @Operation(summary = "Get Profile")
//    @SecurityRequirements
    public ResponseEntity<?> showUserProfile(@PathVariable String username) {
        try {
            Profile profile = userService.showUserProfile(username);
            return new ResponseEntity<>(profile, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{username}/follow")
    @Operation(summary = "Follow user")
    public ResponseEntity<?> followUser(@PathVariable("username") String username) {
        try {
            userService.followOrUnfollowUser(username, "follow");
            return new ResponseEntity<>("User was followed successfully!", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{username}/unfollow")
    @Operation(summary = "Unfollow user")
    public ResponseEntity<?> unfollowUser(@PathVariable("username") String username) {
        try {
            userService.followOrUnfollowUser(username, "unfollow");
            return new ResponseEntity<>("User was unfollowed successfully!", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
