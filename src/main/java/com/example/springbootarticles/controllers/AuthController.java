package com.example.springbootarticles.controllers;

import com.example.springbootarticles.models.*;
import com.example.springbootarticles.repositories.UserRepository;
import com.example.springbootarticles.services.CustomService;
import com.example.springbootarticles.services.JwtHelper;
import com.example.springbootarticles.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("api")
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomService customService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private JwtHelper helper;

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/users/login")
    @Operation(summary = "Authentication")
    @SecurityRequirements
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {

        this.doAuthenticate(request.getUsername(), request.getPassword());
        runConfigureCustom();

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());
        String token = this.helper.generateToken(userDetails);

        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .username(userDetails.getUsername()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/users")
    @Operation(summary = "Registration")
    @SecurityRequirements
    public ResponseEntity<User> register(@RequestBody UserRequest registrationUser) {
        try {
            if (userService.checkUserDuplicate(registrationUser.getUsername(), registrationUser.getEmail())) {
                logger.warn("User with username {} or email {} already exists",
                        registrationUser.getUsername(), registrationUser.getEmail());
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            Subscription subscription = new Subscription(
                    5,
                    customService.addMonthToCurrentDate(),
                    customService.addMonthToCurrentDate(),
                    "Free"
            );
            User user = new User(
                    null,
                    registrationUser.getName(),
                    registrationUser.getUsername(),
                    registrationUser.getBio(),
                    registrationUser.getEmail(),
                    passwordEncoder().encode(registrationUser.getPassword()),
                    new Date(),
                    new Date(),
                    new String[]{},
                    new String[]{},
                    new String[]{},
                    subscription
            );
            userRepo.save(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.warn("Exception occurred while registering user : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public void runConfigureCustom() {
        try {
            authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        } catch (Exception e) {
            logger.warn("Exception occurred while configuring AuthenticationManagerBuilder : {}", e.getMessage());
        }
    }

    private void doAuthenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password);
        try {
            manager.authenticate(authentication);
            userService.checkAndUpdateUserSubscription(username);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }
    }

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid !!";
    }

}
