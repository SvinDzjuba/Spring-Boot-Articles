package com.example.springbootarticles.controllers;

import com.example.springbootarticles.models.User;
import com.example.springbootarticles.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/users")
    public List<User> getUsers(){
        return userRepo.findAll();
    }

    @PostMapping("/users")
    public String saveUser(@RequestBody User user){
        userRepo.save(user);
        return "Added user with email: " + user.getEmail();
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") String id, @RequestBody User user)
    {
        Optional<User> userData = userRepo.findById(id);

        if (userData.isPresent()){
            User _user = userData.get();
            _user.setName(user.getName());
            _user.setEmail(user.getEmail());
            _user.setPassword(user.getPassword());
            _user.setCreated_at(user.getCreated_at());
            _user.setUpdated_at(user.getUpdated_at());
            _user.setSubscription(user.getSubscriptionInfo());

            return new ResponseEntity<>(userRepo.save(_user), HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable String id){
        userRepo.deleteById(id);
        return "User with id:{" + id + "} was deleted successfully";
    }
}
